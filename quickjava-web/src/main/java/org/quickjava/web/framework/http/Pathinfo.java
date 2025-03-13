package org.quickjava.web.framework.http;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/19 21:16
 * @projectName quickjava
 */
public class Pathinfo {

    public String url;

    public String hostname;

    public String protocol;

    public Integer port;

    public String path;

    public String file;

    public String query;

    public Map<String, String> queryData = new HashMap<>();

    public String module;

    public String controller;

    public String action;

    public Pathinfo(String originalUrl, URL url) {
        this.url = originalUrl;
        this.protocol = url.getProtocol();
        this.hostname = url.getHost();
        this.port = url.getPort();
        this.path = url.getPath();
        this.file = url.getFile();
        this.query = url.getQuery();
        this.queryData = this.parseQuery(this.query);
    }

    public Pathinfo(String originalUrl, URI uri) {
        this.url = originalUrl;
        this.protocol = uri.getScheme();
        this.hostname = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
        this.query = uri.getQuery();
        this.queryData = this.parseQuery(this.query);
    }

    /**
     * #quickLang 解析url参数
     * @param query
     * @return
     */
    public static Map<String, String> parseQuery(String query)
    {
        Map<String, String> data = new HashMap<>();
        if (query != null && !"".equals(query)) {
            for (String str : query.split("&")) {
                int idx = str.indexOf("=");
                if (idx == -1) continue;
                data.put(str.substring(0, idx), str.substring(idx + 1));
            }
        }
        return data;
    }

    /**
     * #quickLang 将path解析/module/module/action
     * @param module 默认模块
     * @param controller 默认控制器
     * @param action 默认方法
     */
    public void parseControllerAction(String module, String controller, String action)
    {
        String[] paths = path.split("/");
        if (paths.length <= 1) {
            this.module = module;
            this.controller = controller;
            this.action = action;
        } else if (paths.length == 2) {
            this.module = paths[1];
            this.controller = controller;
            this.action = action;
        } else if (paths.length == 3) {
            this.module = paths[1];
            this.controller = paths[2];
            this.action = action;
        } else {
            this.module = paths[1];
            this.controller = paths[2];
            this.action = paths[3];
        }
    }

    /**
     * #quickLang 按Pathinfo模式解析url
     * @param str url
     * @return Pathinfo
     */
    public static Pathinfo parseFromPath(String str)
    {
        try {
            return new Pathinfo(str, new URI(str));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    /**
     * #quickLang 按Pathinfo模式解析url
     * @param str url
     * @return Pathinfo
     */
    public static Pathinfo parseFromUrl(String str)
    {
        try {
            return new Pathinfo(str, new URL(str));
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Pathinfo{" +
                "url='" + url + '\'' +
                ", hostname='" + hostname + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                ", file='" + file + '\'' +
                ", query='" + query + '\'' +
                ", queryData=" + queryData +
                ", module='" + module + '\'' +
                ", module='" + controller + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
