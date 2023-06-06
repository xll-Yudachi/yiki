package com.yudachi.yiki.gateway.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.yudachi.yiki.common.Constants.RouteContants;
import lombok.Data;
import org.springframework.util.AntPathMatcher;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * @Author yudachi
     * @Description 验证请求唯一性
     **/
    public static boolean verifyParams(String param){
        int lastIndex = param.lastIndexOf("&");
        String newParam = param.substring(0, lastIndex);
        String rightSign = param.substring(lastIndex + 1);
        String sign = "sign=" +  DigestUtil.sha256Hex(sortUrlParams(newParam));
        return ObjectUtil.equals(sign, rightSign);
    }

    private static String sortUrlParams(String param){
        List<UrlParam> sortList = Arrays.asList(param.split("&")).stream().map(item -> {
            UrlParam urlParam = new UrlParam();
            urlParam.setKey(item.split("=")[0]);

            if ("timestamp".equals(urlParam.getKey())){
                urlParam.setValue(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
            }else{
                urlParam.setValue(item.split("=")[1]);
            }
            return urlParam;
        }).sorted(Comparator.comparing(UrlParam::getKey)).collect(Collectors.toList());

        StringBuilder res = new StringBuilder();
        for (UrlParam urlParam : sortList) {
            res.append(urlParam.getKey()).append("=").append(urlParam.getValue()).append("&");
        }
        res.append("secretKey=yudachi");

        return res.toString();
    }

    @Data
    private static class UrlParam{
        private String key;
        private String value;
    }

}
