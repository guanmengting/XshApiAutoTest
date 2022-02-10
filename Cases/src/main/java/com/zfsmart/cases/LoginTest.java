package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.model.InterfaceName;
import com.zfsmart.model.LoginCase;
import com.zfsmart.utils.ApiUrlUtil;
import com.zfsmart.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginTest {
    @BeforeTest(groups = "loginTrue",description = "测试准备工作")
    public void beforeTest(){
        TestConfig.loginUrl = ApiUrlUtil.getUrl(InterfaceName.LOGIN);
        TestConfig.ArchiveTagAddUrl = ApiUrlUtil.getUrl(InterfaceName.ARCHIVE_TAG_ADD);
        TestConfig.ArchiveTagGetListUrl = ApiUrlUtil.getUrl(InterfaceName.ARCHIVE_TAG_GET_LIST);
        TestConfig.ArchiveTagGetInfoUrl = ApiUrlUtil.getUrl(InterfaceName.ARCHIVE_TAG_GET_INFO);
        TestConfig.ArchiveTagEditUrl = ApiUrlUtil.getUrl(InterfaceName.ARCHIVE_TAG_EDIT);
        TestConfig.ArchiveTagDeleteUrl = ApiUrlUtil.getUrl(InterfaceName.ARCHIVE_TAG_DELETE);

        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
    }

    @Test(groups = "loginTrue",description = "用户登录成功接口测试")
    public void loginTrue() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase",1);
        System.out.println(loginCase.toString());
        //发送请求
        Object object = getResult(loginCase);
        //验证结果
        Assert.assertEquals(loginCase.getExpected(),object.toString());
    }

    @Test(groups = "loginFalse",description = "用户登录失败接口测试")
    public void loginFalse() throws IOException {
        SqlSession session = DatabaseUtil.getSqlSession();
        LoginCase loginCase = session.selectOne("loginCase",2);
        System.out.println(loginCase.toString());
        //发送请求
        Object object = getResult(loginCase);
        //验证结果
        Assert.assertEquals(loginCase.getExpected(),object.toString());
    }

    private Object getResult(LoginCase loginCase) throws IOException {
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        JSONObject param = new JSONObject();
        param.put("username",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        param.put("type",loginCase.getType());
        StringEntity entity = new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);

        post.setHeader("content-type","application/json");

        TestConfig.cookieStore = new BasicCookieStore();
        TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();

        HttpResponse response = TestConfig.httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);

        if("200".equals(jsonObject.get("code").toString())){
            JSONObject tokenObject = jsonObject.getJSONObject("data");
            String token = tokenObject.getString("access_token");
            BasicClientCookie cookie = new BasicClientCookie("Authorization",token);
            cookie.setDomain("116.62.113.68");
            cookie.setAttribute(ClientCookie.DOMAIN_ATTR,"true");
            cookie.setPath("/");
            TestConfig.cookieStore.addCookie(cookie);
        }

        return jsonObject.get("code");
    }
}
