package io.github.wisely.web.exception;

import io.github.wisely.core.exception.BaseException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.web.context.Result;
import io.github.wisely.web.i18n.MessageConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DefaultExceptionAdvice {

    public DefaultExceptionAdvice(MessageConvert messageConvert) {
        this.messageConvert = messageConvert;
    }

    MessageConvert messageConvert;

    /**
     * 返回消息转换
     *
     * @param baseException 错误信息
     * @return 转换后的错误信息
     */
    private String messageConvert(BaseException baseException) {
        if (this.messageConvert == null) {
            return baseException.getMessage();
        }

        return messageConvert.messageConvert(baseException.getMessage(), baseException.getObjects());
    }


    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<Result<?>> handleBaseException(BaseException ex) {
        log.error("", ex);
        Result<?> result = Result.error(ex.getCode(), messageConvert(ex));
        return ResponseEntity.status(ex.getHttpStatusCode()).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<?>> handleUnknownException(Exception ex) {
        log.error("", ex);
        // 转换为基础异常
        BaseException baseException =
                new BaseException(ex, CommonExceptionEnum.FAIL.getCode(), CommonExceptionEnum.FAIL.getMessage());
        Result<?> result = Result.error(baseException.getCode(), messageConvert(baseException));
        return ResponseEntity.status(baseException.getHttpStatusCode()).body(result);
    }


}
