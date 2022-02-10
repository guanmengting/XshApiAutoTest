package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.model.ArchiveTag;
import com.zfsmart.model.ArchiveTagAddCase;
import com.zfsmart.utils.CookiesUtil;
import com.zfsmart.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArchiveTagAddTest {
    @Test(dependsOnGroups = "loginTrue",description = "添加标签接口测试")
    public void archiveTagAdd() throws IOException, InterruptedException {
        SqlSession session = DatabaseUtil.getSqlSession();
        ArchiveTagAddCase archiveTagAddCase = session.selectOne("archiveTagAddCase",1);
        System.out.println(archiveTagAddCase.toString());
        //发送请求，获取结果
        Object object = getResult(archiveTagAddCase);
        Assert.assertEquals(archiveTagAddCase.getExpected(),object.toString());

//        Thread.sleep(10000);
//
//        if("200".equals(object.toString())){
//            //验证结果
//            System.out.println(archiveTagAddCase.getName());
//            ArchiveTag archiveTag = session.selectOne("archiveTagAdd",archiveTagAddCase.getName());
//            System.out.println(archiveTag);
//            System.out.println(archiveTag.toString());
//            Assert.assertEquals(archiveTagAddCase.getExpected(),object.toString());
//        }
    }

    private Object getResult(ArchiveTagAddCase archiveTagAddCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.ArchiveTagAddUrl);
        JSONObject param = new JSONObject();
        param.put("name",archiveTagAddCase.getName());
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        //设置头信息
        post.setHeader("content-type","application/json");
        //设置cookies
        Map<String,String> cookies = CookiesUtil.getCookies();
        for (String key : cookies.keySet()) {
            post.setHeader(key,cookies.get(key));
        }
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject.get("code");
    }
}
