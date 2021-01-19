package org.quickjava.framework.config;

import lombok.Data;

/**
 * @author Qlo1062-(QloPC-Msi)
 * @date 2021/1/17 21:07
 * @ProjectName quickjava
 */
@Data
public class DatabaseConfig {

    private Type type = Type.MYSQL;

    private String url;

    private String database;

    private String username;

    private String password;

    public enum Type {
        MYSQL,
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "type=" + type +
                ", url='" + url + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
