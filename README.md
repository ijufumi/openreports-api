# OpenReports API
## Frameworks and libraries.

| Name         | Version | Usage        |
|--------------|---------|--------------|
| Scala        | 3.3.7   | -            |
| Scalatra     | 3.1.2   | All of them. |
| Jetty        | 12.1.7  | Web Server   |
| Servlet API  | 6.1.0   | Web API      |
| slf4j        | 2.0.17  | Logging      |
| logback      | 1.5.32  | Logging      |
| Flyway       | 12.2.0  | Migration    |
| Slick        | 3.6.1   | ORM          |
| PostgreSQL   | 42.7.10 | DB           |
| Google Guice | 7.0.0   | DI           |
| Jedis        | 7.4.0   | Cache(Redis) |
| sttp client  | 4.0.19  | HTTP Client  |
| jXls         | 3.1.0   | Reporting    |
| AWS SDK      | 2.42.22 | Storage(S3)  |
| Auth0        | 3.3.0   | JWT          |

## Features (including not implemented yet)
- [ ] Output reporting.
- [ ] Output scheduled reporting.
- [ ] Supporting template format are xls, xlsx.
- [ ] Supporting output format are xls, xlsx, pdf.
- [ ] Manage users.
- [ ] Send report mail.

## How to set up

### Initialization

To run app in local machine, you need to install [Task](https://taskfile.dev/installation/).


```shell
brew install go-task
```

### Building image

```shell
task build
```

### Starting up

```shell
task up
```

### Stopping down

```shell
task down
```

### Migration

Basically, migration will execute automatically when docker-compose starts.
However, the below command makes you able to execute manually.

```shell
task migrateUp
```

### Drop all tables

```shell
task migrateClean
```

### Tests

```shell
task test
```

## Design

### ER diagram

![ERD](./docs/db/diagram.svg)

### API list

- [x] public
  - [x] login
    - [x] with ID / password
    - [x] Google login
  - [x] health
  - [x] role
- [ ] private
  - [x] common
    - [x] logout
  - [x] members
    - [x] status (member info only)
    - [x] update (name, password)
    - [x] get permissions (including menus and workspace)
    - [x] generate access token
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
  - [ ] scheduled reports
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
  - [x] workspaces
    - [x] get
    - [x] list
    - [x] create
    - [x] update
    - [x] members
      - [x] get
      - [x] list
      - [x] create
      - [x] update
      - [x] delete
    - [x] data sources
      - [x] get
      - [x] list
      - [x] create
      - [x] update
      - [x] delete
    - [ ] Logs
      - [ ] list
      - [ ] delete

### TODO list
- [ ] make relation report and params
- [ ] make relation report and report-group
- [ ] make relation group and report-group
- [ ] add scheduling function
- [ ] add credit to layout.html
- [ ] execute on docker image
- [ ] modify to RESTful application such as SPA
- [ ] add validation for json value
