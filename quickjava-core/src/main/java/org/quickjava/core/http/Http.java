package org.quickjava.core.http;

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
    public enum  ContentType {
        FORM("application/x-www-form-urlencoded"),
        FORM_DATA("multipart/form-data"),
        JSON("application/json"),
        XML("text/xml"),
        BINARY("application/octet-stream"),
        OTHER(null),
        ;

        private String type;

        ContentType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
