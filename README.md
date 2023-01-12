# OpenReports API
## Frameworks and libraries.

| Name         | Version | Usage        |
|--------------|---------|--------------|
| Scala        | 2.12.17 | -            |
| Scalatra     | 2.8.2   | All of them. |
| Servlet API  | 3.1.0   | Web API      |
| slf4j        | 2.0.3   | Logging      |
| logback      | 1.2.3   | Logging      |
 | Flyway       | 9.4.0   | Migration    |
 | Slick        | 3.3.3   | ORM          |
 | PostgresSQL  | 42.4.0  | DB           |
 | Google Guice | 5.1.0   | DI           |
| Scala Cache  | 0.28.0  | Cache        |
| sttp client  | 3.7.0   | HTTP Client  |
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
COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILCDKIT=1 docker-compose up --build
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
    - [ ] status (including menus, groups)
    - [ ] update (name, password)
  - [ ] reports
    - [x] list
    - [x] get
    - [x] create
    - [x] update
    - [x] delete
    - [x] output
    - [ ] parameters
      - [ ] list
      - [ ] create
      - [ ] update
      - [ ] delete
    - [ ] groups
      - [ ] list
      - [ ] create
      - [ ] update
      - [ ] delete
  - [ ] scheduled_reports
    - [ ] list
    - [ ] create
    - [ ] update
    - [ ] delete
  - [ ] templates
    - [x] get
    - [x] list
    - [x] create
    - [x] update
    - [x] delete
  - [ ] workspaces
    - [x] get
    - [x] update
    - [x] members
      - [x] list
      - [x] create
      - [x] update
      - [x] delete
    - [ ] data_sources
      - [ ] list
      - [ ] create
      - [ ] update
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
