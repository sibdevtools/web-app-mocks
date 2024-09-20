# Simple Mock - Web Application Mocks

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

| Code                                   | Value                                                 | Description                        |
|----------------------------------------|-------------------------------------------------------|------------------------------------|
| `web.app.mocks.uri.mock.path`          | `/web/app/mocks/mock/`                                | Url for mocks request              |
| `web.app.mocks.uri.rest.mocks.path`    | `/web/app/mocks/rest/api/services/{serviceId}/mocks/` | Url for Rest mocks request         |
| `web.app.mocks.uri.rest.services.path` | `/web/app/mocks/rest/api/services/`                   | Url for Rest services request      |
| `web.app.mocks.props.bucket.code`      | `mocks`                                               | Used bucket code for mocks storing |
