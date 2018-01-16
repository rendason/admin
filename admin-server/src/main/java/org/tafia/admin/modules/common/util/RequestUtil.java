package org.tafia.admin.modules.common.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dason on 2018/1/13.
 */
public class RequestUtil {

    private RequestUtil() {
    }

    public static String getBasePath(HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        if (contextPath.isEmpty()) contextPath = "/";
        int fromIndex = requestUrl.startsWith("https") ? 8 : 7;
        int endIndex = requestUrl.indexOf(contextPath, fromIndex);
        String basePath = requestUrl.substring(0, endIndex + contextPath.length());
        if (basePath.endsWith("/")) basePath = basePath.substring(0, basePath.length() - 1);
        return basePath;
    }
}
