package com.yudachi.yiki.common.utils;

import com.yudachi.yiki.common.Constants.RouteContants;
import org.springframework.util.AntPathMatcher;

/**
 * @ClassName UrlUtils
 * @Description 请求URL相关工具类
 * @Author Yudachi
 * @Date 2023/6/5 11:02
 * @Version 1.0
 */
public class UrlUtils {

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * @Description 判断是否在白名单
     **/
    public static boolean isSkip(String path) {
        return RouteContants.getDefaultWhiteRoute().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    /**
     * @Description 判断是否是鉴权相关接口
     **/
    public static boolean isAuth(String path) {
        return RouteContants.getOauth2Route().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }


}
