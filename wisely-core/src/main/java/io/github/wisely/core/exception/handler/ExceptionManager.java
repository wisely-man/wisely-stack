package io.github.wisely.core.exception.handler;


import com.google.common.collect.Range;
import com.google.common.collect.TreeRangeMap;
import io.github.wisely.core.exception.*;
import io.github.wisely.core.exception.*;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.helper.ValidHelper;
import lombok.NonNull;

public class ExceptionManager {


    private static final TreeRangeMap<
            @NonNull Integer,
            @NonNull ExceptionBuilderFunction<Integer, String, Object[], BaseException>>
            RANGE_MAP = TreeRangeMap.create();


    static {
        // 9999 默认异常范围
        RANGE_MAP.put(Range.closed(9999, 9999), BaseException::new);
        // [1000, 1999] 系统异常
        RANGE_MAP.put(Range.closed(1000, 1999), SystemException::of);
        // [2000, 2999] 参数校验异常
        RANGE_MAP.put(Range.closed(2000, 2999), ValidationException::of);
        // [3000, 4999] 第三方服务异常
        RANGE_MAP.put(Range.closed(3000, 4999), ThirdPartyException::of);
        // [5000, 9998] 业务异常
        RANGE_MAP.put(Range.closed(5000, 9998), BusinessException::of);
    }

    /**
     * 添加异常范围
     * 如区间冲突，后插入区间的会覆盖先插入的冲突部分部分
     * ex.
     * 依次插入 [0, 100]: "a"  [50, 150]: "b"
     * 结果为 [0, 50): "a"  [50, 150]: "b"
     *
     * @param range    异常范围
     * @param function 异常构建函数
     */
    public static void addRange(Range<@NonNull Integer> range, ExceptionBuilderFunction<Integer, String, Object[], BaseException> function) {

        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(range);
        CommonExceptionEnum.PARAMETER_REQUIRED.assertNotNull(function);

        RANGE_MAP.put(range, function);
    }


    /**
     * 根据错误枚举类自动构建异常
     *
     * @param frameworkCommonAssert 断言对象
     * @param params                错误参数
     */
    public static BaseException buildException(FrameworkCommonAssert frameworkCommonAssert, Object... params) {
        return buildException(frameworkCommonAssert.getCode(), frameworkCommonAssert.getMessage(), params);
    }

    /**
     * 根据错误码自动构建异常
     *
     * @param code    错误代码
     * @param message 错误信息
     * @param params  错误参数
     */
    public static BaseException buildException(int code, String message, Object... params) {
        ExceptionBuilderFunction<Integer, String, Object[], BaseException> function = ExceptionManager.RANGE_MAP.get(code);
        if (ValidHelper.isNotNull(function)) {
            return function.apply(code, message, params);
        }
        throw new BaseException(code, message, params);
    }
}
