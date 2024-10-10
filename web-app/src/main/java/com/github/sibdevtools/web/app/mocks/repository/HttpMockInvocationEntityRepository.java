package com.github.sibdevtools.web.app.mocks.repository;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Http mocks invocations entity repository
 *
 * @author sibmaks
 * @since 0.0.13
 */
public interface HttpMockInvocationEntityRepository extends JpaRepository<HttpMockInvocationEntity, Long> {

    /**
     * Find all mock invocation by mocks id
     *
     * @param mockId   mock identifier
     * @param pageable pageable
     * @return list of mock invocations
     */
    Page<HttpMockInvocationEntity> findAllByMockId(long mockId, Pageable pageable);

}
