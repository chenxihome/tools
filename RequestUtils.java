/*
 * Copyright (C), 2013-2014, 上海汽车集团股份有限公司
 */
package com.saic.ebiz.mall.controller.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.saic.ebiz.mall.constants.PageConstant;
import com.saic.ebiz.mall.util.CookieUtil;

/**
 * @author hejian
 *
 */
public class RequestUtils {

	/***
	 * 获取特定cookie的value
	 * @param request web请求对象
	 * @param cookieKey cookieKey
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest request,String cookieKey){
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		if(!StringUtils.isBlank(cookieKey)){
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if (cookieKey.equals(name)) {
					cookieValue = cookie.getValue();
					break;
				}
			}
		}
		return cookieValue;
	}
	
	/***
	 * 获取请求的完整url路径 比如http://car.chexiang.com/detail.htm?spuId=2222
	 * @param request
	 * @param needQueryString 是否包含get请求的参数
	 * @return
	 */
	public static String getRequestFullUrl(HttpServletRequest request,boolean needQueryString){
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(request.getRequestURL().toString());
		if(needQueryString){
			if(request.getQueryString() != null && request.getQueryString().length() > 0){
				urlBuilder.append(PageConstant.PUNCTUATION_QUESTION_MARK).append(request.getQueryString());
			}
		}
		return urlBuilder.toString();
	}
	/***
	 * 处理传递过来的请求url以符合业务需求  比如http://car.chexiang.com/detail.htm?spuId=2222
	 * @param request
	 * @param needQueryString 是否去除问号后的参数
	 * @return String
	 */
	public static String getRequestUrlAfterDeal(String url,boolean removeQueryString){
		if (StringUtils.isNotBlank(url) && removeQueryString) {
			String cache[] = url.split(PageConstant.PUNCTUATION_QUESTION_MARK_REGEX);
			url = cache[0];
		}
		return url;
	}
	
	/***
	 * 获取请求的城市id
	 * @param request
	 * @return
	 */
	public static String getCityId(HttpServletRequest request) {
		String cookiesCityId = CookieUtil.getClientCookies(request, PageConstant.MALL_CITY_ID_KEY);
    	if (StringUtils.isBlank(cookiesCityId)) {
    		cookiesCityId = CookieUtil.getClientCookies(request, PageConstant.CITY_ID);
    		if (StringUtils.isBlank(cookiesCityId)) {
    			cookiesCityId = PageConstant.SH_CITY_ID;
			}
		}
		return cookiesCityId;
	}
}