package com.github.sibdevtools.web.app.mocks.repository;

import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Http mocks invocations entity repository
 *
 * @author sibmaks
 * @since 0.0.13
 */
public interface HttpMockInvocationEntityRepository extends JpaRepository<HttpMockInvocationEntity, Long> {

    /**
     * Find mock invocation by mock identifier and invocation id
     *
     * @param mockId mock identifier
     * @param id     invocation identifier
     * @return invocation
     */
    Optional<HttpMockInvocationEntity> findByMockIdAndId(long mockId, long id);

    /**
     * Find all mock invocation by mocks id
     *
     * @param mockId   mock identifier
     * @param pageable pageable
     * @return list of mock invocations
     */
    Page<HttpMockInvocationEntity> findAllByMockId(long mockId, Pageable pageable);

    /**
     * Delete all obsolete mock invocations
     *
     * @param bound delete bound
     */
    @Modifying
    int deleteAllByCreatedAtBefore(ZonedDateTime bound);

    /**
     * Count all mock invocations
     *
     * @param mockId mock identifier
     * @return invocation count
     */
    long countAllByMockId(long mockId);

    /**
     * Delete all mock invocations except last N
     *
     * @param mockId mock identifier
     * @param rows   rows to stay alive
     */
    @Modifying
    @Query(value = "DELETE FROM web_app_mocks.http_mock_invocation WHERE id IN ( " +
            "  SELECT id FROM ( " +
            "    SELECT id, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS row_num " +
            "    FROM web_app_mocks.http_mock_invocation " +
            "    WHERE mock_id = :mockId " +
            "  ) ranked WHERE row_num > :rows FOR UPDATE" +
            ")", nativeQuery = true)
    void deleteAllExceptLastN(
            @Param("mockId") long mockId,
            @Param("rows") int rows
    );
}
