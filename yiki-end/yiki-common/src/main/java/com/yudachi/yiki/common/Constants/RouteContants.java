package com.yudachi.yiki.common.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName SecurityConstants
 * @Description TODO
 * @Author Yudachi
 * @Date 2023/6/1 17:05
 * @Version 1.0
 */
public class RouteContants {
    private static String ACC_MEDIA_URL = "/oauth/token";


    public static List<String> getDefaultWhiteRoute(){
        return Arrays.asList(ACC_MEDIA_URL.trim().split(","));
    }
}
