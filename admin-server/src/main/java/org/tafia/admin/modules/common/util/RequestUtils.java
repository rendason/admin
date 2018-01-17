package org.tafia.admin.modules.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dason on 2018/1/13.
 */
public class RequestUtils {

    private RequestUtils() {
    }

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    public static String getBasePath() {
        HttpServletRequest request = getRequest();
        String requestUrl = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        if (contextPath.isEmpty()) contextPath = "/";
        int fromIndex = requestUrl.startsWith("https") ? 8 : 7;
        int endIndex = requestUrl.indexOf(contextPath, fromIndex);
        String basePath = requestUrl.substring(0, endIndex + contextPath.length());
        if (basePath.endsWith("/")) basePath = basePath.substring(0, basePath.length() - 1);
        return basePath;
    }

    public static String getIp() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)){
            int index = ip.indexOf(",");
            return index != -1 ? ip.substring(0, index) : ip;
        }
        ip = request.getHeader("X-Real-IP");
        if(StringUtils.isNotEmpty(ip) && !"unknown".equalsIgnoreCase(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }
}
