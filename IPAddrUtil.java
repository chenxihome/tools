package com.saic.ebiz.mall.controller.util;

import javax.servlet.http.HttpServletRequest;
/**
 * 
 * 〈一句话功能简述〉<br> 
 *  获取用户真实ip
 *
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class IPAddrUtil {
	
	 /**获取用户真实IP地址，不使用request.getRemoteAddr()
	  *的原因是有可能用户使用了代理软件方式避免真实IP地址,   
	  *可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，
	  *而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？    
	  * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。   
	  *如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,   
	  *192.168.1.100  用户真实IP为： 192.168.1.110   
	  *      
	  **/
	public static  String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("x-forwarded-for");

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

		ip = request.getHeader("Proxy-Client-IP");

		}

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

		ip = request.getHeader("WL-Proxy-Client-IP");

		}

		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

		ip = request.getRemoteAddr();

		}

		return ip;

		}
}
