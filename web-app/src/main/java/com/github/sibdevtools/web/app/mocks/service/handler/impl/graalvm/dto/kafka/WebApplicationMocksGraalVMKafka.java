package com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.dto.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sibdevtools.service.kafka.client.api.dto.RecordMetadataDto;
import com.github.sibdevtools.service.kafka.client.api.rq.SendTemplateMessageRq;
import com.github.sibdevtools.service.kafka.client.service.BootstrapGroupService;
import com.github.sibdevtools.service.kafka.client.service.MessageConsumerService;
import com.github.sibdevtools.service.kafka.client.service.MessagePublisherService;
import com.github.sibdevtools.service.kafka.client.service.TemplateMessageService;
import com.github.sibdevtools.web.app.mocks.service.handler.impl.graalvm.GraalVMConverter;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.graalvm.polyglot.HostAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.22
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebApplicationMocksGraalVMKafka {
    protected final BootstrapGroupService bootstrapGroupService;
    protected final MessageConsumerService messageConsumerService;
    protected final MessagePublisherService messagePublisherService;
    protected final TemplateMessageService templateMessageService;

    @Autowired
    @Qualifier("webAppMocksObjectMapper")
    private ObjectMapper objectMapper;

    private static PublishMessageRs getPublishMessageRs(RecordMetadataDto it) {
        return new PublishMessageRs(
                it.getOffset(),
                it.getTimestamp(),
                it.getSerializedKeySize(),
                it.getSerializedValueSize(),
                it.getPartition()
        );
    }

    @HostAccess.Export
    public PublishMessageRs publish(@Nonnull Object arg) {
        var rq = objectMapper.convertValue(arg, PublishMessageRq.class);
        var groupCode = rq.groupCode();
        if (groupCode != null) {
            var bootstrapGroup = bootstrapGroupService.getByCode(groupCode);
            return messagePublisherService.sendMessage(
                            bootstrapGroup.getId(),
                            rq.topic(),
                            rq.partition(),
                            rq.timestamp(),
                            rq.key(),
                            rq.value(),
                            rq.headers(),
                            rq.maxTimeout()
                    )
                    .map(WebApplicationMocksGraalVMKafka::getPublishMessageRs)
                    .orElse(null);
        }
        return messagePublisherService.sendMessage(
                        rq.bootstrapServers(),
                        rq.topic(),
                        rq.partition(),
                        rq.timestamp(),
                        rq.key(),
                        rq.value(),
                        rq.headers(),
                        rq.maxTimeout()
                )
                .map(WebApplicationMocksGraalVMKafka::getPublishMessageRs)
                .orElse(null);
    }

    @HostAccess.Export
    public PublishMessageRs publishTemplate(@Nonnull Object arg) {
        var rq = objectMapper.convertValue(arg, PublishTemplateMessageRq.class);
        var bootstrapGroup = bootstrapGroupService.getByCode(rq.groupCode());
        var templateRsDto = templateMessageService.getByCode(rq.templateCode());
        return templateMessageService.send(
                        templateRsDto.getId(),
                        new SendTemplateMessageRq(
                                bootstrapGroup.getId(),
                                rq.topic(),
                                rq.partition(),
                                rq.timestamp(),
                                rq.key(),
                                GraalVMConverter.convertInput(rq.input()),
                                rq.headers(),
                                rq.maxTimeout()
                        )
                )
                .map(WebApplicationMocksGraalVMKafka::getPublishMessageRs)
                .orElse(null);
    }

    @HostAccess.Export
    public ConsumeMessagesRs getMessages(@Nonnull Object arg) {
        val rq = objectMapper.convertValue(arg, ConsumeMessagesRq.class);
        val bootstrapServers = getBootstrapServers(rq);
        val messages = getConsumerRecords(rq, bootstrapServers);

        val consumed = messages.stream()
                .map(it -> ConsumedMessage.builder()
                        .objectMapper(objectMapper)
                        .partition(it.partition())
                        .offset(it.offset())
                        .timestamp(it.timestamp())
                        .timestampType(it.timestampType().name)
                        .serializedKeySize(it.serializedKeySize())
                        .serializedValueSize(it.serializedValueSize())
                        .headers(buildHeadersMap(it.headers()))
                        .key(it.key())
                        .value(it.value())
                        .build())
                .toList();

        return ConsumeMessagesRs.builder()
                .topic(rq.topic())
                .messages(consumed)
                .build();
    }

    private List<ConsumerRecord<byte[], byte[]>> getConsumerRecords(
            ConsumeMessagesRq rq,
            List<String> bootstrapServers
    ) {
        if (rq.direction() == ConsumeMessagesRq.Direction.EARLIEST) {
            return messageConsumerService.getMessages(
                            bootstrapServers,
                            rq.topic(),
                            rq.maxMessages(),
                            rq.maxTimeout()
                    )
                    .orElseGet(Collections::emptyList);
        }
        return messageConsumerService.getLastNMessages(
                        bootstrapServers,
                        rq.topic(),
                        rq.maxMessages(),
                        rq.maxTimeout()
                )
                .orElseGet(Collections::emptyList);

    }

    private List<String> getBootstrapServers(ConsumeMessagesRq rq) {
        val groupCode = rq.groupCode();
        if (groupCode != null) {
            val bootstrapGroup = bootstrapGroupService.getByCode(groupCode);
            return bootstrapGroup.getBootstrapServers();
        }
        return rq.bootstrapServers();
    }

    private Map<String, byte[]> buildHeadersMap(Headers headers) {
        val headersMap = new HashMap<String, byte[]>();

        for (val header : headers) {
            headersMap.put(header.key(), header.value());
        }

        return headersMap;
    }

}
