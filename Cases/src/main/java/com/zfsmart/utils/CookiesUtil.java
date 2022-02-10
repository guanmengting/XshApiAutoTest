package com.zfsmart.utils;

import com.zfsmart.config.TestConfig;
import org.apache.http.cookie.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookiesUtil {
    public static Map<String,String> getCookies(){
        Map<String,String> map = new HashMap<>();
        List<Cookie> cookieList = TestConfig.cookieStore.getCookies();
        for (Cookie cookie : cookieList){
            map.put(cookie.getName(),cookie.getValue());
        }
        return map;
    }
}
