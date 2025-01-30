# SibDevTools - Web Application Mocks

Web service provide ability to create HTTP mocks.

Mocks are grouped into services.

## Features

* Ant path for mocks
* JavaScript response processor
* Static response

## To build

```shell
chmod +x gradlew
./gradlew clean build
```

## Service parameters

| Code                                             | Value                                                 | Description                                   |
|--------------------------------------------------|-------------------------------------------------------|-----------------------------------------------|
| `web.app.mocks.uri.mock.path`                    | `/web/app/mocks/mock/`                                | Url for mocks request                         |
| `web.app.mocks.uri.rest.mocks.path`              | `/web/app/mocks/rest/api/services/{serviceId}/mocks/` | Url for Rest mocks request                    |
| `web.app.mocks.uri.rest.services.path`           | `/web/app/mocks/rest/api/services/`                   | Url for Rest services request                 |
| `web.app.mocks.props.bucket.code`                | `mocks`                                               | Used bucket code for mocks storing            |
| `web.app.mocks.props.invocations.bucket.code`    | `mocks.invocations`                                   | Used bucket code for mock invocations storing |
| `web.app.mocks.props.invocation.history.enabled` | `true`                                                | Invocation history enabler                    |

## Clean Ups

### Obsolete invokes

| Code                                                     | Value           | Description                                                                                 |
|----------------------------------------------------------|-----------------|---------------------------------------------------------------------------------------------|
| `web.app.mocks.props.clean-up.obsolete.enabled`          | `false`         | Enabler                                                                                     |
| `web.app.mocks.props.clean-up.obsolete.cron`             | `0 0 */6 * * *` | Scheduling clean up CRON                                                                    |
| `web.app.mocks.props.clean-up.obsolete.ttl`              | `3`             | Invokes ttl to remove                                                                       |
| `web.app.mocks.props.clean-up.obsolete.ttl-type`         | `DAYS`          | Invokes ttl type to remove:<br/>* DAYS<br/>* HOURS<br/>* MINUTES<br/>* SECONDS<br/>* MILLIS |
| `web.app.mocks.props.clean-up.obsolete.max-delete-batch` | `128`           | Max row's amount to delete per iteration                                                    |

### Store only last N invokes

| Code                                         | Value   | Description              |
|----------------------------------------------|---------|--------------------------|
| `web.app.mocks.props.clean-up.count.enabled` | `false` | Enabler                  |
| `web.app.mocks.props.clean-up.count.max`     | `10`    | Max invocations per mock |
