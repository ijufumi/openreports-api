# OpenReports powered by Skinny Framework
## Frameworks and libraries.

| Name | Version | Notes |
| --- |---------| --- |
| Scala | 2.13.8  | - |
| Skinny Framework | 4.0.0   | All of them. |
| Servlet API | 3.1.0   | Web API |
| Skinny Logback | 1.0.14  | Logging |
| SLF4J | 1.7.25  | Logging |
| jXls | 2.4.4   | Reporting |
| jXls POI | 1.0.14  | Reporting |
| JODConverter | 4.2.0   | PDF output |

## Features (not implemented yet)
- [x] Output reporting.
- [ ] Output scheduled reporting.
- [x] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [x] Manage users and groups.
- [ ] Send report mail.

## Developer's memo.
### API list

- public
  - login
- private
  - common
    - logout
    - status (including menus, groups)
  - reports
    - list
    - output
  - scheduled_reports
    - list
    - create
    - update
    - delete
  - settings
    - reports
      - list
      - create
      - update
      - delete
      - templates
        - list
        - create
        - delete
      - parameters
        - list
        - create
        - update
        - delete
      - users
        - list
        - create
        - update
        - delete
        - groups
          - list
          - create
          - update
          - delete

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

### TODO list
- [x] make relation report and params
- [x] make relation report and report-group
- [x] make relation group and report-group
- [ ] add scheduling function
- [ ] add credit to layout.html
- [ ] execute on docker image
- [ ] modify to RESTful application such as SPA
- [ ] add validation for json value
