package com.github.sibdevtools.web.app.mocks.service.handler.impl.js.dto;

import com.github.sibdevtools.session.api.dto.SessionId;
import com.github.sibdevtools.session.api.dto.SessionOwnerType;
import com.github.sibdevtools.session.api.rq.CreateSessionRq;
import com.github.sibdevtools.session.api.service.SessionService;
import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.graalvm.polyglot.HostAccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@AllArgsConstructor
public class JsSessions {
    private final SessionService sessionService;

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId) {
        var getSessionRs = sessionService.get(sessionId);
        var session = getSessionRs.getBody();
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId, long version) {
        var getSessionRs = sessionService.get(SessionId.of(sessionId, version));
        var session = getSessionRs.getBody();
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession get(@Nonnull String sessionId, String versionCode) {
        var version = Long.parseLong(versionCode);
        var getSessionRs = sessionService.get(SessionId.of(sessionId, version));
        var session = getSessionRs.getBody();
        return new JsSession(sessionService, session.getId());
    }

    @HostAccess.Export
    public JsSession create(@Nonnull Map<String, Map<String, Serializable>> sections,
                            @Nonnull String ownerTypeCode,
                            @Nonnull String ownerId,
                            @Nonnull List<String> permissions) {
        var ownerType = SessionOwnerType.valueOf(ownerTypeCode);
        var rq = CreateSessionRq.builder()
                .sections(sections)
                .ownerType(ownerType)
                .ownerId(ownerId)
                .permissions(permissions)
                .build();
        var createSessionRs = sessionService.create(rq);
        var sessionId = createSessionRs.getBody();
        var getSessionRs = sessionService.get(sessionId);
        var session = getSessionRs.getBody();
        return new JsSession(sessionService, session.getId());
    }

}
