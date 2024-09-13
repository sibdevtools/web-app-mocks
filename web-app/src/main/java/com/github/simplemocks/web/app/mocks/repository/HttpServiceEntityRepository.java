package com.github.simplemocks.web.app.mocks.repository;

import com.github.simplemocks.web.app.mocks.entity.HttpServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Http services entity repository
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface HttpServiceEntityRepository extends JpaRepository<HttpServiceEntity, Long> {
}
