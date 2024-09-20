CREATE SCHEMA IF NOT EXISTS web_app_mocks;

SET SCHEMA web_app_mocks;

ALTER TABLE http_mock
    ALTER COLUMN ant_pattern
        RENAME TO path;
