# OpenReports API
## Frameworks and libraries.

| Name         | Version | Usage        |
|--------------|---------|--------------|
| Scala        | 2.12.18 | -            |
| Scalatra     | 2.8.4   | All of them. |
| Servlet API  | 4.0.1   | Web API      |
| slf4j        | 2.0.7   | Logging      |
| logback      | 1.4.7   | Logging      |
| Flyway       | 9.22.3  | Migration    |
| Slick        | 3.4.1   | ORM          |
| PostgresSQL  | 42.6.0  | DB           |
| Google Guice | 7.0.0   | DI           |
| Scala Cache  | 0.28.0  | Cache        |
| sttp client  | 3.8.15  | HTTP Client  |
| jXls         | 2.12.0  | Reporting |

## Features (not implemented yet)
- [ ] Output reporting.
- [ ] Output scheduled reporting.
- [ ] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [ ] Manage users.
- [ ] Send report mail.

## How to set up

### Starting up

```shell
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILCDKIT=1 docker compose up --build
```

### Migration

Basically, migration will execute automatically when docker-compose starts.
However, the below command makes you able to execute manually.

```shell
docker-compose exec api sbt flywayMigrate
```

### Drop all tables

```shell
docker-compose exec api sbt flywayClean
```

## Design

### ER diagram

![ERD](./docs/db/diagram.svg)

## Developer's memo.
### API list

- [ ] public
  - [x] login
    - [x] with ID / password
    - [x] Google login
  - [x] health
  - [x] permission
- [ ] private
  - [x] common
    - [x] logout
  - [ ] members
    - [x] status (member info only)
    - [ ] ~~status (including menus, groups)~~
    - [x] update (name, password)
  - [x] reports
    - [x] list
    - [x] get
    - [x] create
    - [x] update
    - [x] delete
    - [x] output
  - [ ] report parameters
    - [ ] list
    - [ ] create
    - [ ] update
    - [ ] delete
  - [x] report groups
    - [x] list
    - [x] create
    - [x] update
    - [x] delete
  - [ ] scheduled_reports
    - [ ] list
    - [ ] create
    - [ ] update
    - [ ] delete
  - [x] templates
    - [x] get
    - [x] list
    - [x] create
    - [x] update
    - [x] delete
  - [ ] workspaces
    - [x] get
    - [ ] list
    - [x] create
    - [x] update
    - [x] members
      - [x] get
      - [x] list
      - [x] create
      - [x] update
      - [x] delete
    - [x] data_sources
      - [x] get
      - [x] list
      - [x] create
      - [x] update
      - [x] delete
    - [ ] Logs
      - [ ] list
      - [ ] delete

### Table Structure

- Users
  - workspaces
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
  - data_sources
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
