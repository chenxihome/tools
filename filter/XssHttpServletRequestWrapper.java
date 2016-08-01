/*
 * Copyright (C), 2013-2014, 上海汽车集团股份有限公司
 * FileName: XssHttpServletRequestWrapper.java
 * Author:   chenliang
 * Date:     2014年3月11日 下午7:27:22
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.saic.ebiz.web.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meidusa.fastjson.JSONObject;

/**
 * request装饰类，防止Xss/csrf/注入钓鱼网站/SQL等的攻击.<br>
 * 由于网站允许用户密码特殊字符，所有对password、newPassword不予过滤
 * 
 * @author chenliang
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

    /**
     * Instantiates a new xss http servlet request wrapper.
     * 
     * @param servletRequest the servlet request
     */
    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    /**
     * {@inheritDoc}
     */
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        // 用户密码则直接返回
        if ("password".equalsIgnoreCase(parameter) || "newPassword".equalsIgnoreCase(parameter)) {
            return values;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        LOGGER.debug("防攻击替换原字符串: {} - {}", parameter, JSONObject.toJSON(values));
        // 替换request中敏感字符
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        LOGGER.debug("防攻击替换新字符串: {} - {}", parameter, JSONObject.toJSON(values));
        return encodedValues;
    }

    /**
     * {@inheritDoc}
     */
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        // 用户密码则直接返回
        if ("password".equalsIgnoreCase(parameter) || "newPassword".equalsIgnoreCase(parameter)) {
            return value;
        }

        LOGGER.debug("防攻击替换原字符串: {} - {}", parameter, value);
        LOGGER.debug("防攻击替换新字符串: {} - {}", parameter, cleanXSS(value));
        return cleanXSS(value);
    }

    /**
     * {@inheritDoc}
     */
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }

        LOGGER.debug("防攻击替换原字符串: {} - {}", name, value);
        LOGGER.debug("防攻击替换新字符串: {} - {}", name, cleanXSS(value));
        return cleanXSS(value);
    }

    /**
     * 功能描述: 替换敏感字符<br>
     * .
     * 
     * @param value 需要替换的字串
     * @return 替换好的字串
     */
    private String cleanXSS(String value) {
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\(", "&#40;")
                .replaceAll("\\)", "&#41;").replaceAll("'", "&#39;").replaceAll("eval\\((.*)\\)", "")
                .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        return value;
    }
}
