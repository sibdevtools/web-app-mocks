package com.github.sibdevtools.web.app.mocks.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.storage.api.dto.BucketFile;
import com.github.sibdevtools.storage.api.rq.SaveFileRq;
import com.github.sibdevtools.storage.api.service.StorageService;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationDto;
import com.github.sibdevtools.web.app.mocks.api.mock.dto.HttpMockInvocationItemDto;
import com.github.sibdevtools.web.app.mocks.constant.Constants;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockEntity;
import com.github.sibdevtools.web.app.mocks.entity.HttpMockInvocationEntity;
import com.github.sibdevtools.web.app.mocks.event.spring.InvocationCreatedEvent;
import com.github.sibdevtools.web.app.mocks.exception.NotFoundException;
import com.github.sibdevtools.web.app.mocks.exception.UnexpectedErrorException;
import com.github.sibdevtools.web.app.mocks.mapper.HttpMockInvocationDtoMapper;
import com.github.sibdevtools.web.app.mocks.mapper.HttpMockInvocationItemDtoMapper;
import com.github.sibdevtools.web.app.mocks.repository.HttpMockInvocationEntityRepository;
import com.github.sibdevtools.web.app.mocks.utils.HttpUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.13
 */
@Slf4j
@Service
public class WebAppMockInvocationService {
    private final HttpMockInvocationEntityRepository httpMockInvocationEntityRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;
    private final HttpMockInvocationDtoMapper httpMockInvocationDtoMapper;
    private final HttpMockInvocationItemDtoMapper httpMockInvocationItemDtoMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${web.app.mocks.props.invocations.bucket.code}")
    private String bucketCode;

    @Autowired
    public WebAppMockInvocationService(
            HttpMockInvocationEntityRepository httpMockInvocationEntityRepository,
            StorageService storageService,
            @Qualifier("webAppMocksObjectMapper")
            ObjectMapper objectMapper,
            HttpMockInvocationDtoMapper httpMockInvocationDtoMapper,
            HttpMockInvocationItemDtoMapper httpMockInvocationItemDtoMapper,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.httpMockInvocationEntityRepository = httpMockInvocationEntityRepository;
        this.storageService = storageService;
        this.objectMapper = objectMapper;
        this.httpMockInvocationDtoMapper = httpMockInvocationDtoMapper;
        this.httpMockInvocationItemDtoMapper = httpMockInvocationItemDtoMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Get invocation history by mock id
     *
     * @param mockId   mock identifier
     * @param page     history page
     * @param pageSize history page size
     * @return invocation history
     */
    public Page<HttpMockInvocationItemDto> getHistory(long mockId,
                                                      Integer page,
                                                      Integer pageSize) {
        var pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        var invocations = httpMockInvocationEntityRepository.findAllByMockId(mockId, pageable);

        var entities = invocations
                .stream()
                .map(httpMockInvocationItemDtoMapper::map)
                .toList();

        return new PageImpl<>(entities, pageable, invocations.getTotalElements());
    }

    /**
     * Get mock invocation by id
     *
     * @param mockId       mock identifier
     * @param invocationId invocation identifier
     * @return invocation
     */
    public HttpMockInvocationDto getInvocation(long mockId, long invocationId) {
        var invocation = httpMockInvocationEntityRepository.findByMockIdAndId(mockId, invocationId)
                .orElseThrow(() -> new NotFoundException("Invocation %s not found".formatted(invocationId)));

        var rqBucketFile = getBucketFile(invocation.getRqBodyStorageId());
        var rsBucketFile = getBucketFile(invocation.getRsBodyStorageId());

        return httpMockInvocationDtoMapper.map(invocation, rqBucketFile, rsBucketFile);
    }

    private BucketFile getBucketFile(String storageId) {
        if (storageId == null) {
            return null;
        }
        var getBucketFileRs = storageService.get(storageId);
        return getBucketFileRs.getBody();
    }

    /**
     * Save mock invocation
     *
     * @param timing invocation timing
     * @param path   invocation path
     * @param mock   invoked mock
     * @param rq     http request
     * @param rs     http response
     */
    @Transactional(
            isolation = Isolation.REPEATABLE_READ,
            propagation = Propagation.REQUIRES_NEW
    )
    public void save(long timing,
                     String path,
                     HttpMockEntity mock,
                     HttpServletRequest rq,
                     HttpServletResponse rs) {

        var entityBuilder = HttpMockInvocationEntity.builder()
                .mockId(mock.getId())
                .remoteHost(rq.getRemoteHost())
                .remoteAddress(rq.getRemoteAddr())
                .method(rq.getMethod())
                .path(path)
                .timing(timing)
                .status(rs.getStatus())
                .createdAt(ZonedDateTime.now());

        try {
            storeRqData(rq, mock, entityBuilder);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't store request data", e);
        }

        try {
            storeRsData(rs, mock, entityBuilder);
        } catch (IOException e) {
            throw new UnexpectedErrorException("Can't store response data", e);
        }

        httpMockInvocationEntityRepository.save(
                entityBuilder.build()
        );
        applicationEventPublisher.publishEvent(new InvocationCreatedEvent(mock.getId()));
    }

    private void storeRqData(HttpServletRequest rq,
                             HttpMockEntity mock,
                             HttpMockInvocationEntity.HttpMockInvocationEntityBuilder entityBuilder) throws IOException {
        var body = HttpUtils.getRqBody(rq);
        var headers = HttpUtils.getHeaders(rq);
        var queryParams = HttpUtils.getQueryParams(rq);

        var saveRq = SaveFileRq.builder()
                .bucket(bucketCode)
                .name(mock.getName())
                .meta(Map.of(
                        Constants.META_HTTP_HEADERS, objectMapper.writeValueAsString(headers),
                        Constants.META_QUERY_PARAMS, objectMapper.writeValueAsString(queryParams)
                ))
                .data(body)
                .build();
        var saveFileRs = storageService.save(saveRq);
        var contentId = saveFileRs.getBody();

        entityBuilder
                .rqBodyStorageType(bucketCode)
                .rqBodyStorageId(contentId);
    }

    private void storeRsData(HttpServletResponse rs,
                             HttpMockEntity mock,
                             HttpMockInvocationEntity.HttpMockInvocationEntityBuilder entityBuilder) throws IOException {
        var body = HttpUtils.getRsBody(rs);
        var headers = HttpUtils.getHeaders(rs);

        var saveRq = SaveFileRq.builder()
                .bucket(bucketCode)
                .name(mock.getName())
                .meta(Map.of(
                        Constants.META_HTTP_HEADERS, objectMapper.writeValueAsString(headers)
                ))
                .data(body)
                .build();
        var saveFileRs = storageService.save(saveRq);
        var contentId = saveFileRs.getBody();

        entityBuilder
                .rsBodyStorageType(bucketCode)
                .rsBodyStorageId(contentId);
    }
}
