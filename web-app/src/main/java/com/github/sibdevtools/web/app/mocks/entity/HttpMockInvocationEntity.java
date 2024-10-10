package com.github.sibdevtools.web.app.mocks.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * Http mock invocation JPA entity
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Entity(name = "web_app_mocks_http_mock_invocation")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "web_app_mocks", name = "http_mock_invocation")
public class HttpMockInvocationEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "mock_id", nullable = false)
    private long mockId;
    @Column(name = "remote_host")
    private String remoteHost;
    @Column(name = "remote_address")
    private String remoteAddress;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "path", nullable = false)
    private String path;
    @Column(name = "timing", nullable = false)
    private long timing;
    @Column(name = "status", nullable = false)
    private int status;
    @Column(name = "rq_body_storage_type")
    private String rqBodyStorageType;
    @Column(name = "rq_body_storage_id")
    private String rqBodyStorageId;
    @Column(name = "rs_body_storage_type")
    private String rsBodyStorageType;
    @Column(name = "rs_body_storage_id")
    private String rsBodyStorageId;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
}
