package org.quickjava.framework.http;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/18 23:45
 * @projectName quickjava
 */
public class Http {

    /**
     * @langCn header头key，包含request和response
     */
    public class HeaderKey {
        public static final String Accept = "accept";
        public static final String Accept_Charset = "accept-charset";
        public static final String Accept_Encoding = "accept-encoding";
        public static final String Accept_Language = "accept-language";
        public static final String Host = "host";
        public static final String User_Agent = "user-agent";
        public static final String Age = "age";
        public static final String Server = "server";
        public static final String Accept_Ranges = "accept-ranges";
        public static final String Allow = "allow";
        public static final String Location = "location";
        public static final String Content_Language = "content-language";
        public static final String Content_Length = "content-length";
        public static final String Content_Type = "content-type";
        public static final String Cookie = "cookie";
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
     * @langCn HTTP状态码
     */
    public class Status {
        public static final int SUCCESS = 200;
    }
}
