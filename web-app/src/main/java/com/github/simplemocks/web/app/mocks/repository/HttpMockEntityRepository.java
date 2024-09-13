package com.github.simplemocks.web.app.mocks.repository;

import com.github.simplemocks.web.app.mocks.entity.HttpMockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Http mocks entity repository
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface HttpMockEntityRepository extends JpaRepository<HttpMockEntity, Long> {

    /**
     * Find all mocks by http method and service code
     *
     * @param method      http method code
     * @param serviceCode http service code
     * @return list of mocks
     */
    List<HttpMockEntity> findAllByMethodAndServiceCode(String method, String serviceCode);

    /**
     * Find all mocks by service id
     *
     * @param serviceId service identifier
     * @return list of mocks
     */
    List<HttpMockEntity> findAllByServiceId(long serviceId);

}
