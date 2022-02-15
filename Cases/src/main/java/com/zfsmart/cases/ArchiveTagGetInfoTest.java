package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.utils.CookiesUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ArchiveTagGetInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取标签详情接口测试")
    public void archiveTagGetInfo() throws IOException {
        System.out.println();
        ArchiveTagGetListTest test = new ArchiveTagGetListTest();
        Map<String,Object> map = (Map<String,Object>) test.getResult(null);
        ArrayList<Map<String,Object>> list = (ArrayList<Map<String,Object>>) map.get("actualList");
        Map<String,Object> tagMap = list.get(0);
        //发送请求
        Object object = getResult((Integer) tagMap.get("id"));
        //验证结果
        Assert.assertEquals("200",object.toString());
//        for (Map<String,Object> tagMap : list) {
//            //发送请求
//            Object object = getResult((Integer) tagMap.get("id"));
//            //验证结果
//            Assert.assertEquals("200",object.toString());
//        }
    }

    public Object getResult(int id) throws IOException {
        System.out.println(TestConfig.ArchiveTagGetInfoUrl + "/" + id);
        HttpGet get = new HttpGet(TestConfig.ArchiveTagGetInfoUrl + "/" + id);
        //设置cookies
        Map<String,String> cookies = CookiesUtil.getCookies();
        for (String key : cookies.keySet()) {
            get.setHeader(key,cookies.get(key));
        }
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
        HttpResponse response = TestConfig.httpClient.execute(get);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.get("code");
    }
}
