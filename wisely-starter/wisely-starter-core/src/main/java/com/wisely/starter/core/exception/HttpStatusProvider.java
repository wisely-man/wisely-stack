package com.wisely.starter.core.exception;

public interface HttpStatusProvider {

    /**
     * 获取HTTP状态码，默认500
     *
     * @return HTTP状态码
     */
    default int getHttpStatusCode() {
        return 503;
    }

}
