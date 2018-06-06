## OpenReports powered by Skinny Framework

### Features (not implemented yet)
- [ ] Output reporting.
- [ ] Output scheduled reporting.
- [ ] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [ ] Manage users and groups.
- [ ] Send report mail.

### Menu/Function tree

- public
  - Top
    - Login
    - Sign Up
- private
  - Home
  - Report
    - Reporting
      - List
      - Output
    - Scheduled Reporting
      - List
      - Add
      - Edit
  - Setting
    - Report
      - List
      - Upload template file
    - Report Parameters
      - List
      - Add
      - Edit
    - Group
      - List
      - Add
      - Edit
    - User
      - List
      - Add
      - Edit

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
  - a