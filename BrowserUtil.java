/*
 * Copyright (C), 2013-2014, 上海汽车集团股份有限公司
 */
package com.saic.ebiz.mall.controller.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 浏览器工具类
 * 
 * @author hejian
 *
 */
public class BrowserUtil {
	
	/**
	 * 判断是否是微信浏览器
	 * @param request
	 * @return true 是微信浏览器 false 不是微信浏览器
	 */
	public static boolean isWeixinBrowser(HttpServletRequest request){
		
		boolean isWx = false;
		/**
		 * 获取用户浏览器类型
		 */
		String userAgent = request.getHeader("User-Agent");
		
		/**
		 * 判断是否为微信浏览器
		 */
		if(userAgent != null && userAgent.contains("MicroMessenger")){
			isWx = true;
		}
		
		return isWx;
	}
}
