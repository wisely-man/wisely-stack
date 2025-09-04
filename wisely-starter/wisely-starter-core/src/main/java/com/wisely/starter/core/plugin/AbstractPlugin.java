package com.wisely.starter.core.plugin;

import lombok.extern.slf4j.Slf4j;

/**
 * 插件基类
 */
@Slf4j
public abstract class AbstractPlugin {

    public AbstractPlugin() {
        printLog();
    }

    protected abstract String getName();

    protected void printLog() {
        log.info("Plugin [{}] has been enabled.", getName());
    }

}
