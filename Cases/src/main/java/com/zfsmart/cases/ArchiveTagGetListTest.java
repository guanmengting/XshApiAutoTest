package com.zfsmart.cases;

import com.zfsmart.config.TestConfig;
import com.zfsmart.utils.ApiUrlUtil;
import com.zfsmart.utils.CookiesUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ArchiveTagGetListTest {
    @Test(dependsOnGroups = "loginTrue",description = "添加标签接口测试")
    public void archiveTagGetList() throws IOException {
        //发送请求
        Map<String,Object> map = (Map<String,Object>) getResult(null);
        //验证结果
        Assert.assertEquals(((Integer) map.get("totalCount")).intValue(),((ArrayList<Map<String,Object>>) map.get("actualList")).size());
    }

    public Object getResult(String name) throws IOException {
        Map<String,String> cookies = CookiesUtil.getCookies();

        ArrayList<Map<String,Object>> list = new ArrayList<>();
        int totalCount = 0;
        int totalPage = 0;
        int pageNum = 1;
        int pageSize = 10;
        do {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("pageNum",pageNum);
            params.put("pageSize",pageSize);
            if(name != null && !name.isEmpty()){
                params.put("name",name);
            }
//            System.out.println(ApiUrlUtil.getHttpGetUrl(TestConfig.ArchiveTagGetListUrl,params));
            HttpGet get = new HttpGet(ApiUrlUtil.getHttpGetUrl(TestConfig.ArchiveTagGetListUrl,params));
            //设置cookies
            for (String key : cookies.keySet()) {
                get.setHeader(key,cookies.get(key));
            }
            TestConfig.httpClient = HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();
            HttpResponse response = TestConfig.httpClient.execute(get);
            String result = EntityUtils.toString(response.getEntity(),"utf-8");
//            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            if("200".equals(jsonObject.get("code").toString())){
                if(totalPage == 0){
                    totalCount = ((Integer)jsonObject.get("total")).intValue();
                    totalPage = (totalCount + pageSize -1) / pageSize;
                }
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Map<String,Object> tagMap = new HashMap<>();
                    tagMap.put("id",obj.get("id"));
                    tagMap.put("name",obj.get("name"));
                    list.add(tagMap);
                }
                pageNum ++;
            }
        } while (pageNum <= totalPage);
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",totalCount);
        map.put("actualList",list);
        return map;
    }

}
