package com.akuacom.pss2.dlc;

import javax.servlet.http.HttpServletResponse;

public class ServiceUtil {
    public static void notAllowed(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    public static void notFound(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
