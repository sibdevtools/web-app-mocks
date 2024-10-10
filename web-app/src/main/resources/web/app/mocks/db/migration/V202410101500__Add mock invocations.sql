CREATE SCHEMA IF NOT EXISTS web_app_mocks;

SET SCHEMA web_app_mocks;

CREATE TABLE IF NOT EXISTS http_mock_invocation
(
    id                bigint        NOT NULL AUTO_INCREMENT,
    mock_id           bigint        NOT NULL,
    remote_host       varchar(256),
    remote_address    varchar(256),
    method            varchar(32)   NOT NULL,
    path              varchar(1024) NOT NULL,
    timing            bigint        NOT NULL,
    status            int           NOT NULL,
    body_storage_type varchar(64)   NOT NULL,
    body_storage_id   varchar(128)  NOT NULL,
    created_at        timestamp     NOT NULL,
    CONSTRAINT http_mock_invocation_pk PRIMARY KEY (id),
    FOREIGN KEY (mock_id) references http_mock (id)
);