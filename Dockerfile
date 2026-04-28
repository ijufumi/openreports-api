# syntax=docker/dockerfile:1.7
# ============================================================
# Stage 1: build assembly jar
# ============================================================
FROM eclipse-temurin:25-jdk-noble AS builder

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl ca-certificates gnupg \
    && install -d -m 0755 /etc/apt/keyrings \
    && curl -fsSL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" \
        | gpg --dearmor -o /etc/apt/keyrings/sbt.gpg \
    && echo "deb [signed-by=/etc/apt/keyrings/sbt.gpg] https://repo.scala-sbt.org/scalasbt/debian all main" \
        > /etc/apt/sources.list.d/sbt.list \
    && apt-get update \
    && apt-get install -y --no-install-recommends sbt \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /build

COPY project ./project
COPY build.sbt ./
RUN sbt update

COPY src ./src
COPY reports ./reports
RUN sbt assembly \
    && cp target/scala-*/open-reports-api.jar /build/open-reports-api.jar

# ============================================================
# Stage 2: runtime
# ============================================================
FROM eclipse-temurin:25-jre-noble

ENV DEBIAN_FRONTEND=noninteractive \
    APP_DIR=/app

RUN apt-get update \
    && apt-get install -y --no-install-recommends postgresql-client \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR ${APP_DIR}

COPY --from=builder /build/open-reports-api.jar ./open-reports-api.jar
COPY src/main/webapp ./src/main/webapp
COPY reports ./reports
COPY scripts/entrypoint-prod.sh ./scripts/entrypoint-prod.sh
RUN chmod +x ./scripts/entrypoint-prod.sh

EXPOSE 8080

ENTRYPOINT ["bash", "/app/scripts/entrypoint-prod.sh"]
