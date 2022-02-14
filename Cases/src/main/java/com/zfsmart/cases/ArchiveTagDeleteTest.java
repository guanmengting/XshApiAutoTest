package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.model.ArchiveTag;
import com.zfsmart.model.ArchiveTagDeleteCase;
import com.zfsmart.utils.CookiesUtil;
import com.zfsmart.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ArchiveTagDeleteTest {
    @Test(dependsOnGroups = "loginTrue",description = "添加标签接口测试")
    public void archiveTagDelete() throws IOException {
        System.out.println();
        ArchiveTagDeleteCase archiveTagDeleteCase = TestConfig.session.selectOne("archiveTagDeleteCase",1);
        System.out.println(archiveTagDeleteCase.toString());

        ArchiveTagGetListTest test = new ArchiveTagGetListTest();
        Map<String,Object> map = (Map<String,Object>) test.getResult(archiveTagDeleteCase.getName() + "-autoEdit");
        ArrayList<Map<String,Object>> list = (ArrayList<Map<String,Object>>) map.get("actualList");
        String ids = "";
        for (int i=0;i<Math.min(list.size(),2);i++) {
            Map<String,Object> tagMap = list.get(i);
            if(!ids.isEmpty()){
                ids += ",";
            }
            ids += tagMap.get("id").toString();
        }

        //发送请求
        Object object = getResult(ids);
        //验证结果
        Assert.assertEquals(archiveTagDeleteCase.getExpected(),object.toString());

        Map<String,Object> map2 = (Map<String,Object>) test.getResult(archiveTagDeleteCase.getName() + "-autoEdit");
        ArrayList<Map<String,Object>> list2 = (ArrayList<Map<String,Object>>) map2.get("actualList");
        Assert.assertEquals(0,list2.size());
    }

    @Test(dependsOnMethods = {"archiveTagDelete"},description = "校验数据新增")
    public void archiveTagDeleteCheck() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        ArchiveTagDeleteCase archiveTagDeleteCase = session.selectOne("archiveTagDeleteCase",1);
        ArchiveTag archiveTag = session.selectOne("archiveTag",archiveTagDeleteCase.getName() + "-autoEdit");
        Assert.assertNull(archiveTag);
    }

    private Object getResult(String ids) throws IOException {
        System.out.println(TestConfig.ArchiveTagDeleteUrl + "/" + ids);
        HttpDelete delete = new HttpDelete(TestConfig.ArchiveTagDeleteUrl + "/" + ids);
        //设置头信息
        delete.setHeader("content-type","application/json");
        //设置cookies
        Map<String,String> cookies = CookiesUtil.getCookies();
        for (String key : cookies.keySet()) {
            delete.setHeader(key,cookies.get(key));
        }

        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
        HttpResponse response = TestConfig.httpClient.execute(delete);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.get("code");
    }
}
