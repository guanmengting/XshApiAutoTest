package com.zfsmart.config;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.ibatis.session.SqlSession;

public class TestConfig {
    public static String loginUrl;
    public static String ArchiveTagAddUrl;
    public static String ArchiveTagGetListUrl;
    public static String ArchiveTagGetInfoUrl;
    public static String ArchiveTagEditUrl;
    public static String ArchiveTagDeleteUrl;

    public static HttpClient httpClient;
    public static CookieStore cookieStore;
    public static SqlSession session;
}
