# OpenReports API
## Frameworks and libraries.

| Name         | Version | Notes        |
|--------------|---------|--------------|
| Scala        | 2.13.8  | -            |
| Scalatra     | 2.8.2   | All of them. |
| Servlet API  | 3.1.0   | Web API      |
| logback      | 1.2.3   | Logging      |
 | Flyway       | 8.5.13  | Migration    |
 | Slick        | 3.3.3   | ORM          |
 | PostgresSQL  | 42.4.0  | DB           |
 | Google Guice | 5.1.0   | DI           |
| Scala Cache  | 0.28.0   | Cache        |
| sttp client  | 3.7.0   | HTTP Client  |

## Features (not implemented yet)
- [ ] Output reporting.
- [ ] Output scheduled reporting.
- [ ] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [ ] Manage users and groups.
- [ ] Send report mail.

## How to set up

### Starting up

```shell
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILCDKIT=1 docker-compose up --build
```

### Migration

Basically, migration will execute automatically when docker-compose starts.
However, the below command makes you able to execute manually.

```shell
docker-compose exec api sbt flywayMigrate
```

## Developer's memo.
### API list

- public
  - [ ] login
    - [x] with ID / password
    - [ ] Google login
- private
  - common
    - [ ] logout
    - [ ] status (including menus, groups)
  - reports
    - [ ] list
    - [ ] output
  - scheduled_reports
    - [ ] list
    - [ ] create
    - [ ] update
    - [ ] delete
  - settings
    - reports
      - [ ] list
      - [ ] create
      - [ ] update
      - [ ] delete
      - templates
        - [ ] list
        - [ ] create
        - [ ] delete
      - parameters
        - [ ] list
        - [ ] create
        - [ ] update
        - [ ] delete
      - users
        - [ ] list
        - [ ] create
        - [ ] update
        - [ ] delete
        - groups
          - [ ] list
          - [ ] create
          - [ ] update
          - [ ] delete

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
- [ ] make relation report and params
- [ ] make relation report and report-group
- [ ] make relation group and report-group
- [ ] add scheduling function
- [ ] add credit to layout.html
- [ ] execute on docker image
- [ ] modify to RESTful application such as SPA
- [ ] add validation for json value
