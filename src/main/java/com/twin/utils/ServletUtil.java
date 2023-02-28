package com.twin.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

    public static ServletRequestAttributes getServletRequestAttr() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletResponse getServletResponse() {
        ServletRequestAttributes attributes = getServletRequestAttr();
        return attributes.getResponse();
    }
}
