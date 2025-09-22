package io.github.wisely.web.i18n;

import io.github.wisely.core.helper.StringHelper;
import io.github.wisely.core.helper.ValidHelper;
import io.github.wisely.web.context.helper.RequestHelper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;

@Slf4j
public class DefaultI18nMessageConvert implements MessageConvert {


    @Resource
    MessageSource messageSource;

    /**
     * 拼接业务参数
     *
     * @param message 消息
     * @param objects 参数
     * @return 拼接后的消息
     */
    @Override
    public String messageConvert(String message, Object... objects) {
        try {
            // try to use i18n
            if (StringHelper.isBlank(message) || messageSource == null) {
                return message;
            }

            Locale locale = null;
            HttpServletRequest request = RequestHelper.getRequest();
            if (ValidHelper.isNotNull(request)) {
                locale = RequestContextUtils.getLocale(RequestHelper.getRequest());
            } else {
                locale = Locale.getDefault();
            }

            log.debug("locale ==> {}", locale);
            return messageSource.getMessage(message, objects, locale);

        } catch (Exception e) {
            // ignore it, it's fine
            log.trace("DefaultI18nMessageConvert.messageConvert failed:", e);
        }
        return message;
    }

}
