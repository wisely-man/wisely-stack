package io.github.wisely.web.i18n;

public interface MessageConvert {


    /**
     *
     * @param message 消息
     * @param objects 参数
     * @return 转换后的消息
     */
    String messageConvert(String message, Object... objects);

}
