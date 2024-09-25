package com.github.sibdevtools.web.app.mocks.constant;

import com.github.sibdevtools.error.api.dto.ErrorSourceId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final ErrorSourceId ERROR_SOURCE = new ErrorSourceId("WEB.APP.MOCKS");

}
