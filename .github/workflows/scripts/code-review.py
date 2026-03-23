"""
Claude Code Review Script
=========================
GitHub PR上でClaudeによるコードレビューを実施し、
指摘事項をMUST / SHOULD / NICE TO HAVEで分類してコメントする。

フロー:
  1. レビュー開始コメント投稿 + "レビュー中" ラベル付与
  2. PR差分を取得し、Claude APIでレビュー実行
  3. ファイルごとにインラインコメントを投稿
  4. サマリコメントを投稿 + ラベル除去
"""

import json
import os
import re
import sys
from textwrap import dedent

from anthropic import Anthropic
from github import Github

# ---------------------------------------------------------------------------
# 設定
# ---------------------------------------------------------------------------
GITHUB_TOKEN = os.environ["GITHUB_TOKEN"]
ANTHROPIC_API_KEY = os.environ["ANTHROPIC_API_KEY"]
PR_NUMBER = int(os.environ["PR_NUMBER"])
REPO_NAME = os.environ["REPO_NAME"]
CLAUDE_MODEL = os.environ.get("CLAUDE_MODEL", "claude-sonnet-4-20250514")

REVIEW_LABEL = "🤖 Claude レビュー中"

SEVERITY_EMOJI = {
  "MUST": "🔴 **[MUST]**",
  "SHOULD": "🟡 **[SHOULD]**",
  "NICE_TO_HAVE": "🟢 **[NICE TO HAVE]**",
}

SEVERITY_DESCRIPTION = {
  "MUST": "修正必須 — バグ、セキュリティ問題、重大な設計上の問題など",
  "SHOULD": "修正推奨 — 保守性・可読性・パフォーマンスの改善など",
  "NICE_TO_HAVE": "検討推奨 — 軽微なスタイル改善、追加テストの提案など",
}

# レビュー対象外の拡張子（必要に応じて追加）
SKIP_EXTENSIONS = {
  ".lock", ".sum", ".mod", ".min.js", ".min.css",
  ".png", ".jpg", ".jpeg", ".gif", ".ico", ".svg",
  ".woff", ".woff2", ".ttf", ".eot",
  ".zip", ".tar", ".gz",
}

# 1ファイルあたりの最大行数（大きすぎるdiffはスキップ）
MAX_DIFF_LINES = 500

# リポジトリ共通レビュー観点ファイルのパス
REVIEW_GUIDELINES_PATH = "docs/REVIEW.md"

# PR description 内でPR固有のレビュー観点を囲むマーカー
# PR作成者は <!-- REVIEW_FOCUS --> ... <!-- /REVIEW_FOCUS --> の間に記述する
PR_REVIEW_FOCUS_START = "<!-- REVIEW_FOCUS -->"
PR_REVIEW_FOCUS_END = "<!-- /REVIEW_FOCUS -->"


# ---------------------------------------------------------------------------
# GitHub ヘルパー
# ---------------------------------------------------------------------------
def get_pr():
  gh = Github(GITHUB_TOKEN)
  repo = gh.get_repo(REPO_NAME)
  return repo, repo.get_pull(PR_NUMBER)


def post_comment(pr, body: str):
  """PRにコメントを投稿"""
  return pr.create_issue_comment(body)


def add_label(pr, label: str):
  """ラベルを追加（存在しなければ作成）"""
  try:
    pr.add_to_labels(label)
  except Exception:
    # ラベルが存在しない場合は作成してから付与
    repo = pr.base.repo
    try:
      repo.create_label(name=label, color="FFA500")
    except Exception:
      pass
    try:
      pr.add_to_labels(label)
    except Exception:
      pass


def remove_label(pr, label: str):
  """ラベルを除去"""
  try:
    pr.remove_from_labels(label)
  except Exception:
    pass


def get_diff_files(pr):
  """PRの変更ファイルとパッチ(diff)を取得"""
  files = []
  for f in pr.get_files():
    ext = os.path.splitext(f.filename)[1].lower()
    if ext in SKIP_EXTENSIONS:
      continue
    if f.patch is None:
      continue
    patch_lines = f.patch.split("\n")
    if len(patch_lines) > MAX_DIFF_LINES:
      continue
    files.append(
      {
        "filename": f.filename,
        "status": f.status,  # added / modified / removed
        "patch": f.patch,
      }
    )
  return files


def load_repo_review_guidelines(repo) -> str:
  """
  リポジトリ共通のレビュー観点を docs/REVIEW.md から読み込む。
  ファイルが存在しない場合は空文字を返す。
  """
  try:
    content = repo.get_contents(REVIEW_GUIDELINES_PATH, ref=repo.default_branch)
    text = content.decoded_content.decode("utf-8")
    print(f"[INFO] リポジトリ共通レビュー観点を読み込み ({len(text)} 文字)")
    return text
  except Exception as e:
    print(f"[INFO] {REVIEW_GUIDELINES_PATH} が見つかりません（スキップ）: {e}")
    return ""


def extract_pr_review_focus(pr_body: str) -> str:
  """
  PR description から <!-- REVIEW_FOCUS --> ... <!-- /REVIEW_FOCUS --> で
  囲まれたPR固有のレビュー観点を抽出する。
  マーカーがなければ空文字を返す。
  """
  if not pr_body:
    return ""

  pattern = re.compile(
    re.escape(PR_REVIEW_FOCUS_START)
    + r"(.*?)"
    + re.escape(PR_REVIEW_FOCUS_END),
    re.DOTALL,
    )
  match = pattern.search(pr_body)
  if match:
    text = match.group(1).strip()
    print(f"[INFO] PR固有レビュー観点を抽出 ({len(text)} 文字)")
    return text

  print("[INFO] PR固有レビュー観点マーカーなし（スキップ）")
  return ""


# ---------------------------------------------------------------------------
# Claude レビュー
# ---------------------------------------------------------------------------
SYSTEM_PROMPT_BASE = dedent("""\
    あなたは経験豊富なシニアソフトウェアエンジニアです。
    GitHub PRのコード差分をレビューし、指摘事項を以下の3段階で分類してください。

    ## 分類基準
    - **MUST**: 修正必須。バグ、セキュリティ脆弱性、データ損失リスク、重大なロジックエラー、
      本番障害につながる可能性がある問題。
    - **SHOULD**: 修正推奨。可読性・保守性の低下、パフォーマンス改善の余地、
      エラーハンドリング不足、テスト不足など。
    - **NICE_TO_HAVE**: 検討推奨。コードスタイルの軽微な改善、命名の微調整、
      コメント追加の提案、リファクタリングのアイデアなど。

    ## 出力形式
    以下のJSON形式で出力してください。JSON以外は出力しないでください。

    ```json
    {
      "reviews": [
        {
          "filename": "変更ファイルのパス",
          "line": 該当行番号（diffの+行の行番号）,
          "severity": "MUST" | "SHOULD" | "NICE_TO_HAVE",
          "comment": "指摘内容（日本語で簡潔に）",
          "suggestion": "改善案のコードまたは説明（任意、なければnull）"
        }
      ],
      "overall_summary": "PR全体に対する総評（日本語で2〜3文）"
    }
    ```

    ## 注意事項
    - 問題がなければ reviews を空配列にしてください。
    - line はdiffの中で `+` が付いている行（追加行）の、ファイル内での行番号を指定してください。
    - 些細すぎる指摘は控え、本当に価値のあるフィードバックに絞ってください。
    - セキュリティに関わる問題は必ず MUST にしてください。
""")


def build_system_prompt(repo_guidelines: str, pr_review_focus: str) -> str:
  """
  システムプロンプトを動的に構築する。
  リポジトリ共通レビュー観点とPR固有レビュー観点がある場合は追記する。
  """
  parts = [SYSTEM_PROMPT_BASE]

  if repo_guidelines:
    parts.append(dedent("""\

            ## リポジトリ共通レビュー観点
            以下はこのリポジトリで定められたレビュー観点です。
            差分をレビューする際、これらの観点にも必ず照らし合わせて指摘してください。
            共通レビュー観点に違反している場合は、指摘コメント内で該当する観点を明示してください。

        """))
    parts.append(repo_guidelines)
    parts.append("\n")

  if pr_review_focus:
    parts.append(dedent("""\

            ## PR固有レビュー観点
            以下はこのPR特有のレビュー観点として、PR作成者が指定したものです。
            通常のレビューに加え、これらの観点を特に重点的にチェックしてください。
            該当する指摘がある場合は、コメント内でPR固有観点に関する指摘であることを明示してください。

        """))
    parts.append(pr_review_focus)
    parts.append("\n")

  return "".join(parts)


def build_review_prompt(diff_files: list, pr_title: str, pr_body: str) -> str:
  """Claude に渡すユーザープロンプトを構築"""
  parts = [
    f"# PR: {pr_title}\n",
  ]
  if pr_body:
    parts.append(f"## 説明\n{pr_body[:2000]}\n")

  parts.append("## 変更差分\n")
  for f in diff_files:
    parts.append(f"### {f['filename']} ({f['status']})\n```diff\n{f['patch']}\n```\n")

  return "\n".join(parts)


def call_claude(prompt: str, system_prompt: str) -> dict:
  """Claude API を呼び出してレビュー結果を取得"""
  client = Anthropic(api_key=ANTHROPIC_API_KEY)
  response = client.messages.create(
    model=CLAUDE_MODEL,
    max_tokens=4096,
    system=system_prompt,
    messages=[{"role": "user", "content": prompt}],
  )

  text = response.content[0].text.strip()

  # JSON部分を抽出（```json ... ``` で囲まれている場合に対応）
  if "```json" in text:
    text = text.split("```json")[1].split("```")[0].strip()
  elif "```" in text:
    text = text.split("```")[1].split("```")[0].strip()

  return json.loads(text)


# ---------------------------------------------------------------------------
# コメント投稿
# ---------------------------------------------------------------------------
def post_inline_comments(pr, reviews: list):
  """
  各指摘をPRのインラインコメントとして投稿する。
  create_review を使い、1回のAPIコールでまとめて投稿。
  """
  commit = list(pr.get_commits())[-1]  # 最新コミット
  comments = []

  for r in reviews:
    severity_tag = SEVERITY_EMOJI.get(r["severity"], r["severity"])
    body = f"{severity_tag}\n\n{r['comment']}"
    if r.get("suggestion"):
      body += f"\n\n💡 **改善案:**\n```suggestion\n{r['suggestion']}\n```"

    comments.append(
      {
        "path": r["filename"],
        "line": r["line"],
        "body": body,
      }
    )

  if not comments:
    return

  # GitHub の create_review でまとめて投稿
  # side="RIGHT" は省略（デフォルトで追加側）
  try:
    pr.create_review(
      commit=commit,
      body="🤖 Claude によるコードレビューのインライン指摘です。",
      event="COMMENT",
      comments=[
        {
          "path": c["path"],
          "position": _find_diff_position(pr, c["path"], c["line"]),
          "body": c["body"],
        }
        for c in comments
        if _find_diff_position(pr, c["path"], c["line"]) is not None
      ],
    )
  except Exception as e:
    # インラインコメントが失敗した場合は通常コメントにフォールバック
    print(f"[WARN] create_review failed: {e}")
    fallback_body = "## 🤖 Claude コードレビュー（インライン投稿失敗のためまとめて表示）\n\n"
    for c in comments:
      fallback_body += f"**{c['path']}** (L{c.get('line', '?')})\n{c['body']}\n\n---\n\n"
    post_comment(pr, fallback_body)


def _find_diff_position(pr, filename: str, target_line: int):
  """
  diffの中でのposition（1始まりの相対位置）を算出する。
  GitHub APIのreviewコメントは、diff内の相対位置（position）を要求する。
  """
  for f in pr.get_files():
    if f.filename != filename:
      continue
    if f.patch is None:
      return None

    position = 0
    current_line = 0

    for diff_line in f.patch.split("\n"):
      position += 1
      if diff_line.startswith("@@"):
        # @@ -a,b +c,d @@ のパース
        try:
          plus_part = diff_line.split("+")[1].split("@@")[0].strip()
          current_line = int(plus_part.split(",")[0]) - 1
        except (IndexError, ValueError):
          continue
      elif diff_line.startswith("-"):
        # 削除行はファイル行番号を進めない
        continue
      else:
        current_line += 1
        if current_line == target_line:
          return position

  return None


def build_summary_comment(reviews: list, overall_summary: str) -> str:
  """サマリコメントを構築"""
  # 分類ごとに集計
  categorized = {"MUST": [], "SHOULD": [], "NICE_TO_HAVE": []}
  for r in reviews:
    severity = r.get("severity", "NICE_TO_HAVE")
    if severity not in categorized:
      severity = "NICE_TO_HAVE"
    categorized[severity].append(r)

  total = len(reviews)
  counts = {k: len(v) for k, v in categorized.items()}

  lines = [
    "## 🤖 Claude コードレビュー サマリ",
    "",
    "### 📊 概要",
    "",
    f"| 分類 | 件数 |",
    f"|------|------|",
    f"| 🔴 MUST（修正必須） | {counts['MUST']} |",
    f"| 🟡 SHOULD（修正推奨） | {counts['SHOULD']} |",
    f"| 🟢 NICE TO HAVE（検討推奨） | {counts['NICE_TO_HAVE']} |",
    f"| **合計** | **{total}** |",
    "",
  ]

  if overall_summary:
    lines.extend(["### 💬 総評", "", overall_summary, ""])

  # 各分類の詳細
  for severity_key, label in [
    ("MUST", "🔴 MUST（修正必須）"),
    ("SHOULD", "🟡 SHOULD（修正推奨）"),
    ("NICE_TO_HAVE", "🟢 NICE TO HAVE（検討推奨）"),
  ]:
    items = categorized[severity_key]
    lines.append(f"### {label}")
    lines.append("")
    if not items:
      lines.append("指摘なし ✅")
      lines.append("")
      continue

    lines.append(f"| # | ファイル | 行 | 指摘内容 |")
    lines.append(f"|---|---------|-----|---------|")
    for i, r in enumerate(items, 1):
      comment_short = r["comment"].replace("\n", " ")[:100]
      lines.append(
        f"| {i} | `{r['filename']}` | L{r['line']} | {comment_short} |"
      )
    lines.append("")

  # フッター
  if total == 0:
    lines.append("> ✅ 特に問題は検出されませんでした。LGTMです！")
  elif counts["MUST"] > 0:
    lines.append(
      "> ⚠️ **MUST** の指摘があります。マージ前に修正を検討してください。"
    )
  else:
    lines.append(
      "> 💡 重大な問題は検出されませんでした。SHOULD / NICE TO HAVE の指摘を確認してください。"
    )

  lines.extend([
    "",
    "---",
    f"*Reviewed by Claude ({CLAUDE_MODEL})*",
  ])

  return "\n".join(lines)


# ---------------------------------------------------------------------------
# メイン処理
# ---------------------------------------------------------------------------
def main():
  repo, pr = get_pr()

  # ---- Step 1: レビュー開始を通知 ----
  print("[INFO] レビュー開始コメントを投稿")
  start_comment = post_comment(
    pr,
    "## 🤖 Claude コードレビューを開始します…\n\n"
    "差分を解析中です。完了次第サマリを投稿します。しばらくお待ちください。\n\n"
    f"*Model: {CLAUDE_MODEL}*",
  )
  add_label(pr, REVIEW_LABEL)

  # ---- Step 2: レビュー観点を読み込み ----
  print("[INFO] レビュー観点を読み込み")
  repo_guidelines = load_repo_review_guidelines(repo)
  pr_review_focus = extract_pr_review_focus(pr.body or "")

  # ---- Step 3: 差分を取得 ----
  print("[INFO] PR差分を取得")
  diff_files = get_diff_files(pr)
  if not diff_files:
    print("[INFO] レビュー対象ファイルなし")
    post_comment(pr, "## 🤖 Claude コードレビュー完了\n\nレビュー対象のコード変更が見つかりませんでした。")
    remove_label(pr, REVIEW_LABEL)
    return

  print(f"[INFO] レビュー対象: {len(diff_files)} ファイル")

  # ---- Step 4: Claude API でレビュー ----
  print("[INFO] Claude API を呼び出し")
  system_prompt = build_system_prompt(repo_guidelines, pr_review_focus)
  prompt = build_review_prompt(diff_files, pr.title, pr.body or "")
  try:
    result = call_claude(prompt, system_prompt)
  except Exception as e:
    print(f"[ERROR] Claude API エラー: {e}")
    post_comment(pr, f"## 🤖 Claude コードレビュー\n\n❌ レビュー中にエラーが発生しました。\n\n```\n{e}\n```")
    remove_label(pr, REVIEW_LABEL)
    sys.exit(1)

  reviews = result.get("reviews", [])
  overall_summary = result.get("overall_summary", "")

  print(f"[INFO] 指摘件数: {len(reviews)}")

  # ---- Step 5: インラインコメントを投稿 ----
  if reviews:
    print("[INFO] インラインコメントを投稿")
    post_inline_comments(pr, reviews)

  # ---- Step 6: サマリコメントを投稿 ----
  print("[INFO] サマリコメントを投稿")
  summary = build_summary_comment(reviews, overall_summary)
  post_comment(pr, summary)

  # ---- Step 7: 開始コメントを更新してラベル除去 ----
  try:
    start_comment.edit(
      "## 🤖 Claude コードレビュー完了 ✅\n\n"
      "レビューが完了しました。下記のサマリおよびインラインコメントをご確認ください。"
    )
  except Exception:
    pass
  remove_label(pr, REVIEW_LABEL)
  print("[INFO] レビュー完了")


if __name__ == "__main__":
  main()
