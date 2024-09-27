package com.github.sibdevtools.web.app.mocks.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

/**
 * Http mock JPA entity
 *
 * @author sibmaks
 * @since 0.0.1
 */
@Entity(name = "web_app_mocks_http_mock")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "web_app_mocks", name = "http_mock")
public class HttpMockEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "path", nullable = false)
    private String path;
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private HttpServiceEntity service;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "storage_type", nullable = false)
    private String storageType;
    @Column(name = "storage_id", nullable = false)
    private String storageId;
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;
    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    /**
     * Fill values for new entity
     */
    @PrePersist
    public void prePersist() {
        createdAt = ZonedDateTime.now();
        modifiedAt = ZonedDateTime.now();
    }

    /**
     * Fill values for updated entity
     */
    @PreUpdate
    public void preUpdate() {
        modifiedAt = ZonedDateTime.now();
    }
}
