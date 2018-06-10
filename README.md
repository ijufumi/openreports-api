## OpenReports powered by Skinny Framework
### Frameworks and libraries.

| Name | Version | Coment |
| --- | --- | --- |
| Scala | 2.12.5 | All of them. |
| Skinny Framework | 2.6.0 | |
| Servlet API | 3.1.0 | |
| Skinny Logback | 1.0.14 | Logging |
| SLF4J | 1.7.25 | Logging |
| jXls | 2.4.4 | |
| jXls JExcel | 1.0.6 | Reporting |
| jXls POI | 1.0.14 | |

### Features (not implemented yet)
- [ ] Output reporting.
- [ ] Output scheduled reporting.
- [ ] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [ ] Manage users and groups.
- [ ] Send report mail.

## Developer's memo.
### Menu/Function tree

- public
  - Top
    - Login
    - Sign Up
- private
  - Home
  - Report
    - Reporting
      - List(Search)
      - Output
    - Scheduled Reporting
      - List(Search)
      - Add
      - Edit(or Show detail)
  - Setting
    - Report
      - List(Search)
      - Upload template file
    - Report Parameters
      - List(Search)
      - Add
      - Edit(or Show detail)
    - Group
      - List(Search)
      - Add
      - Edit(or Show detail)
    - User
      - List(Search)
      - Add
      - Edit(or Show detail)

### Table Structure

- Users
  - member
  - group
  - member/group relation
  - group/report_group relation
- Reports
  - report
  - scheduled_report
  - report_group
  - report/report_group relation
  - report_param
  - report/report_param relation
- Others
  -