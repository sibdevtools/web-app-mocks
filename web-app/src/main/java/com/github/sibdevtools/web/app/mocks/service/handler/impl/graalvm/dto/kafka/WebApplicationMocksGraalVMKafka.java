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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.HostAccess;
import org.springframework.stereotype.Component;

/**
 * @author sibmaks
 * @since 0.0.22
 */
@Slf4j
@Component
@AllArgsConstructor
public class WebApplicationMocksGraalVMKafka {
    protected final ObjectMapper objectMapper;
    protected final BootstrapGroupService bootstrapGroupService;
    protected final MessageConsumerService messageConsumerService;
    protected final MessagePublisherService messagePublisherService;
    protected final TemplateMessageService templateMessageService;

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


}
