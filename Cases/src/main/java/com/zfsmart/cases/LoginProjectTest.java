package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.utils.CookiesUtil;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.Map;

public class LoginProjectTest {
    @Test(dependsOnGroups = "loginTrue",description = "选择项目接口测试")
    public void loginProject() throws IOException {
        HttpGet get = new HttpGet("http://116.62.113.68:8081/backstage/measure/project/login/project?id=1");
        //设置cookies
        Map<String,String> cookies = CookiesUtil.getCookies();
        for (String key : cookies.keySet()) {
            get.setHeader(key,cookies.get(key));
        }
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
        TestConfig.httpClient.execute(get);
    }

}
