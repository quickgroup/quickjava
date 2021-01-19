package org.quickjava.framework.http;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 23:45
 * @projectName quickjava
 */
public class Http {

    /**
     * @langCn header头key
     */
    public class HeaderKey {
        public static final String Accept = "Accept";
        public static final String Accept_Charset = "Accept-Charset";
        public static final String Accept_Encoding = "Accept-Encoding";
        public static final String Accept_Language = "Accept-Language";
        public static final String Host = "Host";
        public static final String User_Agent = "User-Agent";
        public static final String Age = "Age";
        public static final String Server = "Server";
        public static final String Accept_Ranges = "Accept-Ranges";
        public static final String Allow = "Allow";
        public static final String Location = "Location";
        public static final String Content_Language = "Content-Language";
        public static final String Content_Length = "Content-Length";
        public static final String Content_Type = "Content-Type";
        public static final String Cookies = "Cookies";
    }

    /**
     * @langCn 常见ContentType
     */
    public class  ContentType {
        public static final String FORM = "application/x-www-form-urlencoded";
        public static final String FORM_DATA = "multipart/form-data";
        public static final String JSON = "application/json";
        public static final String XML = "text/xml";
        public static final String HTML = "text/html";
        public static final String BINARY = "application/octet-stream";
    }

    /**
     * HTTP状态码
     */
    public class Status {
        public static final int SUCCESS = 200;
    }
}
