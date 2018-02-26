/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */

/**
 * @author anliyong@baidu.com
 * 2014-12-22 下午5:55:57
 */
package com.nais.kid.keeper.helper;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhujiawe@baidu.com
 *         2014-12-22下午5:55:57
 */
public class AppConfigHelper {

    private static Logger logger = LoggerFactory.getLogger(AppConfigHelper.class);

    private static ResourceBundle bundle = null;

    static {
        bundle = ResourceBundle.getBundle("app");
    }

    public static String getString(String key) {
        String value = null;
        try {
            value = bundle.getString(key);
        } catch (Exception e) {
            logger.error(String.format("未在app.properties中发现key为%s的配置信息", key), e);
        }
        return value;
    }

    public static String getString(String key, String defaultValue) {
        String value = defaultValue;
        try {
            String v = bundle.getString(key);
            if (v != null && !"".equals(v)) {
                value = v;
            }
        } catch (Exception e) {
            logger.error(String.format("未在app.properties中发现key为%s的配置信息", key), e);
        }
        return value;
    }

    public static Integer getInteger(String key) {
        String value = getString(key);
        Integer result = null;
        if (value != null) {
            result = Integer.valueOf(value);
        }
        return result;
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        String value = getString(key);
        Integer result = defaultValue;
        if (value != null) {
            result = Integer.valueOf(value);
        }
        return result;
    }

    public static Long getLong(String key, Long defaultValue) {
        String value = getString(key);
        Long result = defaultValue;
        if (value != null) {
            result = Long.valueOf(value);
        }
        return result;
    }

    public static Boolean getBoolean(String key) {
        String value = getString(key);
        Boolean result = null;
        if (value != null) {
            result = Boolean.valueOf(value);
        }
        return result;
    }

    public static Boolean getBoolean(String key, Boolean defaultValue) {
        String value = getString(key);
        Boolean result = defaultValue;
        if (value != null) {
            result = Boolean.valueOf(value);
        }
        return result;
    }

}
