package com.wisely.starter.core.exception;

import com.wisely.starter.core.exception.eum.CommonExceptionEnum;
import com.wisely.starter.core.data.helper.JsonHelper;
import com.wisely.starter.core.helper.ValidHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;


/**
 * 异常基类
 */
@Setter
@Getter
@Slf4j
public class BaseException extends RuntimeException implements HttpStatusProvider {

    @Serial
    private static final long serialVersionUID = 3112694480576916311L;

    /*异常代码*/
    private int code;
    private Object[] objects;

    public BaseException(Throwable rootCause, int code, String message, Object... objects) {
        super(message, rootCause);
        this.code = code;
        this.objects = objects != null ? objects.clone() : null; // 浅拷贝避免外部修改

        if (ValidHelper.isNotEmpty(objects)) {
            log.debug("exception params:{}", JsonHelper.obj2Json(objects));
        }
    }

    public BaseException(int code, String message, Object... objects) {
        this(null, code, message, objects);
    }

    public BaseException(int code, String message) {
        this(null, code, message, (Object[]) null);
    }

    public BaseException(Throwable rootCause, String message, Object... objects) {
        this(rootCause, CommonExceptionEnum.FAIL.getCode(), message, objects);
    }

    public BaseException() {
        this(null, CommonExceptionEnum.FAIL.getCode(), CommonExceptionEnum.FAIL.getMessage(), (Object[]) null);
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * 获取参数副本，避免数组被修改
     *
     * @return 参数数组副本
     */
    public Object[] getArgs() {
        return objects == null ? new Object[0] : objects.clone(); // 只读视图
    }
}
