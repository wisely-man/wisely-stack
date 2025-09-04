package com.wisely.starter.core.spring.helper;

import com.wisely.starter.core.helper.StringHelper;
import com.wisely.starter.core.helper.ValidHelper;
import org.springframework.core.env.Environment;


/**
 * ConfigHelper
 * 基于 ENVIRONMENT
 */
public class ConfigHelper {

    private ConfigHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static Environment INSTANCE;

    public static void setEnvironment(Environment environment) {
        INSTANCE = environment;
    }

    public static Environment getInstance() {
        if (ValidHelper.isNotNull(INSTANCE)) {
            return INSTANCE;
        }

        return (INSTANCE = SpringHelper.getBean(Environment.class));
    }

    public static Integer getInt(String property, int _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Integer.class, _default);
    }

    public static Integer getInt(String property) {
        return getInt(property, 0);
    }

    public static Long getLong(String property) {
        return getLong(property, 0L);
    }

    public static Long getLong(String property, long _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Long.class, _default);
    }

    public static boolean getBoolean(String property, boolean _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, Boolean.class, _default);
    }

    public static boolean getBoolean(String property) {
        return getBoolean(property, false);
    }


    public static String getString(String property, String _default) {
        return getInstance() == null ? _default : getInstance().getProperty(property, _default);
    }

    public static String getString(String property) {
        return getInstance() == null ? null : getInstance().getProperty(property);
    }

    public static Boolean equals(String property, String value) {
        return ValidHelper.isEquals(getString(property), value);
    }

    public static boolean isEmpty(String property) {
        return StringHelper.isEmpty(getString(property));
    }

    public static boolean isNotEmpty(String property) {
        return StringHelper.isNotEmpty(getString(property));
    }
}
