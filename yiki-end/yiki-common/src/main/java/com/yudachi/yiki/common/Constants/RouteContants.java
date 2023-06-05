package com.yudachi.yiki.common.Constants;

import org.springframework.util.StringUtils;

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
    private static String ACC_WHITE_URL = "/oauth/token, /oauth2Api/oauth/token";
    private static String ACC_OAUTH2_URL = "/oauth/token, /oauth2Api/oauth/token";

    public static List<String> getDefaultWhiteRoute(){
        return Arrays.asList(StringUtils.trimAllWhitespace(ACC_WHITE_URL).split(","));
    }

    public static List<String> getOauth2Route(){
        return Arrays.asList(StringUtils.trimAllWhitespace(ACC_OAUTH2_URL).split(","));
    }
}
