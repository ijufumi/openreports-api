## OpenReports powered by Skinny Framework
### Frameworks and libraries.

| Name | Version | Notes |
| --- | --- | --- |
| Scala | 2.12.6 | - |
| Skinny Framework | 2.6.0 | All of them. |
| Servlet API | 3.1.0 | Web API |
| Skinny Logback | 1.0.14 | Logging |
| SLF4J | 1.7.25 | Logging |
| jXls | 2.4.4 | Reporting |
| jXls POI | 1.0.14 | Reporting |
| iText | 5.5.13 | PDF output |

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
  - group/function relation
- Reports
  - report
  - scheduled_report
  - report_group
  - report/report_group relation
  - report_param
  - report_param_config
- Others
  - functions
