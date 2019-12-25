
package com.saic.ebiz.mall.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.saic.ebiz.constant.service.ConstantCodeService;
import com.saic.ebiz.mall.constants.PageConstant;

/**
 * controller包公用的工具类<br>
 * 〈功能详细描述〉
 * 
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public final class ControllerUtil {

    /**
     * 隐藏工具类的构造方法
     */
    private ControllerUtil() {

    }

    /**
     * 校验请求来源是否来自手机 <br>
     * 〈功能详细描述〉
     * 
     * @param request
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean checkMobile(HttpServletRequest request) {
        String regexMatch = "nokia|iphone|ipod|iuc|android|motorola|^mot\\-|softbank|foma|docomo|kddi|up\\.browser|up\\.link|";
        regexMatch += "htc|dopod|blazer|netfront|helio|hosin|huawei|novarra|CoolPad|webos|techfaith|palmsource|";
        regexMatch += "blackberry|alcatel|amoi|ktouch|nexian|samsung|^sam\\-|s[cg]h|^lge|ericsson|philips|sagem|wellcom|bunjalloo|maui|";
        regexMatch += "symbian|smartphone|midp|wap|phone|windows ce|iemobile|^spice|^bird|^zte\\-|longcos|pantech|gionee|^sie\\-|portalmmm|";
        regexMatch += "jig\\s browser|hiptop|ucweb|^ucweb|^benq|haier|^lct|opera\\s*mobi|opera\\*mini|320x320|240x320|176x220";

        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (StringUtils.isBlank(userAgent)) {
            return true;
        } else {
            Pattern p = Pattern.compile(regexMatch);
            Matcher m = p.matcher(userAgent);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断当前日期是否在某日期之后 <br>
     * @param format 日期格式eg:yyyy-MM-dd HH:mm:ss
     * @param dateTime	设定日期 eg:2015-10-25 00:00:00
     * @return true 在某日期后 false 不在
     */
    public static boolean isTimeLater(String format,String dateTime) {
        // 判断当前日期是否在某日期之后
    	Date nowsysdate = new Date();
		SimpleDateFormat formatLink = new SimpleDateFormat(format);
		Date TurnTime = null;
		boolean isTimeLater=false;
		try {
			TurnTime = formatLink.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (null != TurnTime) {
			if (nowsysdate.after(TurnTime)) {
				isTimeLater = true;
			}
		}
		return isTimeLater;
    }
    /**
     * 判断当前日期是否在某日期之后 <br>
     * @author lijialiang
     * @param dateTime	java.util.Date
     * @return true 在某日期后 false 不在
     */
    public static boolean isTimeLaterDate(Date date) {
        // 判断当前日期是否在某日期之后
    	Date nowsysdate = new Date();
		boolean isTimeLater=false;
		if (null != date) {
			if (nowsysdate.after(date)) {
				isTimeLater = true;
			}
		}
		return isTimeLater;
    }
    /**
     * 格式化String转为Date <br>
     * @param format 日期格式eg:yyyy-MM-dd HH:mm:ss
     * @param dateTime	设定日期 eg:2015-10-25 00:00:00
     * @return Date formatDate
     */
    public static Date getFormatDate(String format,String dateTime) {
		SimpleDateFormat formatLink = new SimpleDateFormat(format);
		Date formatDate = null;
		try {
			formatDate = formatLink.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDate;
    }
   
    /**
     * 根据传入的cityId设定城市ID及城市名称写入cookies <br>
     * @return 如无cookies则定位上海
     */
    public static void setCityCookies(HttpServletRequest request, HttpServletResponse response,Long cityId) {
    	Cookie cookies[] = request.getCookies();
    	String cityName = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cityId != 0) {
					if (PageConstant.MALL_CITY_ID_KEY.equals(cookie.getName())) {
						cookie.setValue(cityId.toString());
						cookie.setPath("/");
						response.addCookie(cookie);
					} else if (PageConstant.MALL_CITY_NAME_KEY.equals(cookie.getName())) {
						try {
							cityName = getCityName(cityId.toString());
							;
							cookie.setValue(URLEncoder.encode(cityName, "UTF-8"));
							cookie.setPath("/");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						response.addCookie(cookie);
					}
				} else {
					if (PageConstant.MALL_CITY_ID_KEY.equals(cookie.getName())) {
						cityId = Long.parseLong(cookie.getValue());
						cityName = getCityName(cityId.toString());

					}
				}
			}
		} else {
			if (cityId == 0) {
				cityId = Long.parseLong(PageConstant.SH_CITY_ID);
			}
			cityName = getCityName(cityId.toString());
			;
			Cookie cookie = new Cookie(PageConstant.MALL_CITY_ID_KEY, cityId.toString());
			cookie.setPath("/");
			response.addCookie(cookie);
			try {
				cookie = new Cookie(PageConstant.MALL_CITY_NAME_KEY, URLEncoder.encode(cityName, "UTF-8"));
				cookie.setPath("/");
				response.addCookie(cookie);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
    	
    }
    public static String getCityName(String cityId) {
		String cityName = ConstantCodeService.getRegionNameByCode(cityId);
		if (cityName == null || "".equals(cityName)) {
			return "";
		}

		return cityName.replace("市", "");
	}
    /**
     * 获取某年第某周的周一的日期 <br>
     * @param int year
     * @param int weekindex
     * @return String weekFirstDay 格式eg:M月d日
     */
    public static String getWeekFirstDay(int year, int weekindex) {
		SimpleDateFormat sdf = new SimpleDateFormat(PageConstant.DATE_FORMAT_MD);
        Calendar c = Calendar.getInstance();
        c.setWeekDate(year, weekindex, 1);
		
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 2;
        c.add(Calendar.DATE, -dayOfWeek); // 得到本周的第一天
        String weekFirstDay = sdf.format(c.getTime());
        
        return weekFirstDay;
	}
}
