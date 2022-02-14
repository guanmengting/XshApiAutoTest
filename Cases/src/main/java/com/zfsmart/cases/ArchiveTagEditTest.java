package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.model.ArchiveTag;
import com.zfsmart.model.ArchiveTagEditCase;
import com.zfsmart.utils.CookiesUtil;
import com.zfsmart.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ArchiveTagEditTest {
    @Test(dependsOnGroups = "loginTrue",description = "修改标签接口测试")
    public void archiveTagEdit() throws IOException {
        System.out.println();
        ArchiveTagEditCase archiveTagEditCase = TestConfig.session.selectOne("archiveTagEditCase",1);
        System.out.println(archiveTagEditCase.toString());

        ArchiveTagGetListTest test = new ArchiveTagGetListTest();
        Map<String,Object> map = (Map<String,Object>) test.getResult(archiveTagEditCase.getName());
        ArrayList<Map<String,Object>> list = (ArrayList<Map<String,Object>>) map.get("actualList");
        Map<String,Object> tagMap = list.get(0);

        //发送请求
        Object object = getResult((Integer) tagMap.get("id"),(String) tagMap.get("name") + "-autoEdit");
        //验证结果
        Assert.assertEquals(archiveTagEditCase.getExpected(),object.toString());
    }

    @Test(dependsOnMethods = {"archiveTagEdit"},description = "校验数据新增")
    public void archiveTagEditCheck() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        ArchiveTagEditCase archiveTagEditCase = session.selectOne("archiveTagEditCase",1);
        ArchiveTag archiveTag = session.selectOne("archiveTag",archiveTagEditCase.getName());
        Assert.assertNull(archiveTag);
        archiveTag = session.selectOne("archiveTag",archiveTagEditCase.getName() + "-autoEdit");
        System.out.println(archiveTag.toString());
        Assert.assertNotNull(archiveTag);
    }

    private Object getResult(int id,String name) throws IOException {
        System.out.println(TestConfig.ArchiveTagEditUrl);
        HttpPut put = new HttpPut(TestConfig.ArchiveTagEditUrl);
        JSONObject param = new JSONObject();
        param.put("id",id);
        param.put("name",name);
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        put.setEntity(entity);
        //设置头信息
        put.setHeader("content-type","application/json");
        //设置cookies
        Map<String,String> cookies = CookiesUtil.getCookies();
        for (String key : cookies.keySet()) {
            put.setHeader(key,cookies.get(key));
        }
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
        HttpResponse response = TestConfig.httpClient.execute(put);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.get("code");
    }
}
