package com.github.sibdevtools.web.app.mocks.repository;

import com.github.sibdevtools.web.app.mocks.entity.HttpServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;

/**
 * Http services entity repository
 *
 * @author sibmaks
 * @since 0.0.1
 */
public interface HttpServiceEntityRepository extends JpaRepository<HttpServiceEntity, Long> {

    /**
     * Update service code by id
     *
     * @param id   service id
     * @param code code
     */
    @Modifying
    @Query("update web_app_mocks_http_service set code=:code, modifiedAt=:modifiedAt where id=:id")
    void updateCodeById(@Param("id") long id,
                        @Param("code") String code,
                        @Param("modifiedAt") ZonedDateTime modifiedAt);

}
