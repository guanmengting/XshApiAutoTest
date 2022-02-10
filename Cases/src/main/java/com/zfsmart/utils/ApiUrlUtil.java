package com.zfsmart.utils;

import com.zfsmart.model.InterfaceName;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.*;

public class ApiUrlUtil {
    private static ResourceBundle bundle = ResourceBundle.getBundle("apiUrl-config", Locale.CHINA);

    public static String getUrl(InterfaceName name){
        String address = bundle.getString("test.url");
        String uri = "";
        if(name == InterfaceName.LOGIN){
            uri = bundle.getString("login.uri");
        }
        if(name == InterfaceName.ARCHIVE_TAG_ADD){
            uri = bundle.getString("ArchiveTagAdd.uri");
        }
        if(name == InterfaceName.ARCHIVE_TAG_EDIT){
            uri = bundle.getString("ArchiveTagEdit.uri");
        }
        if(name == InterfaceName.ARCHIVE_TAG_GET_LIST){
            uri = bundle.getString("ArchiveTagGetList.uri");
        }
        if(name == InterfaceName.ARCHIVE_TAG_GET_INFO){
            uri = bundle.getString("ArchiveTagGetInfo.uri");
        }
        if(name == InterfaceName.ARCHIVE_TAG_DELETE){
            uri = bundle.getString("ArchiveTagDelete.uri");
        }
        String testUrl = address + uri;
        return testUrl;
    }

    public static String getHttpGetUrl(String url, Map<String,Object> params) throws IOException {
        if(params != null && !params.isEmpty()){
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for(String key : params.keySet()){
                pairs.add(new BasicNameValuePair(key,params.get(key).toString()));
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs),"UTF-8");
        }
        return url;
    }
}
