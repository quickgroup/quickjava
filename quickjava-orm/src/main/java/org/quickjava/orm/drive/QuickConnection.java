package org.quickjava.orm.drive;

import org.quickjava.orm.ORMContext;
import org.quickjava.orm.domain.DatabaseMeta;
import org.quickjava.orm.query.contain.Label;
import org.quickjava.orm.utils.QueryException;
import org.quickjava.orm.utils.QuickORMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class QuickConnection implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(QuickConnection.class);

    private DatabaseMeta databaseMeta;

    private Connection connection;

    public boolean autoCommit;

    public QuickConnection(DatabaseMeta databaseMeta) {
        this.databaseMeta = databaseMeta;
        this.autoCommit = databaseMeta.autoCommit;
    }

    /**
     * 连接数据库
     *
     * @return 数据库连接
     */
    public QuickConnection connect() {
        logger.info("Connecting to database... {}", this);
        try {
            this.databaseMeta = ORMContext.getDatabaseMeta();
            this.connection = connect(this, this.databaseMeta);
        } catch (Exception e) {
            throw new QueryException(e);
        }
        return this;
    }

    public static Connection connect(QuickConnection quickConnection, DatabaseMeta config) {
        if (quickConnection.connection != null) {
            throw new RuntimeException("重复连接");
        }
        try {
            return ORMContext.getContextPort().getConnection();
        } catch (SQLException e) {
            throw new QueryException("数据库连接失败：" + config.url + "=>" + e.getMessage());
        }
    }

    public void setAutoCommit(boolean autoCommit) {
        try {
            this.autoCommit = autoCommit;
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new QuickORMException(e);
        }
    }

    public void setTransactionIsolation(int level) {
        try {
            connection.setTransactionIsolation(level);
        } catch (SQLException e) {
            throw new QuickORMException(e);
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭数据库链接
     */
    @Override
    public void close() {
        if (connection != null) {
            logger.info("Closing connection... {}", this);
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 数据插入
     *
     * @param sql 语句
     * @return map
     * @throws SQLException e
     */
    public Map<String, Object> insert(String sql, Map<Label, Object> labels)
            throws SQLException {
        Map<String, Object> ret = new LinkedHashMap<>();
        try (Statement statement = connection.createStatement()) {
            // 执行
            int count;
            if (labels != null && labels.containsKey(Label.INSERT_GET_ID)) {
                count = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                // 获取自增
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    List<String> fieldNameArr = prepareField(generatedKeys);
                    if (!fieldNameArr.isEmpty()) {
                        generatedKeys.next();
                        for (int fi = 1; fi <= generatedKeys.getRow(); fi++) {
                            ret.put(fieldNameArr.get(fi - 1), generatedKeys.getObject(fi));
                        }
                    }
                }

            } else {
                count = statement.executeUpdate(sql);
            }
            ret.put("__orm_count", count);
            return ret;
        }
    }

    public Map<String, Object> insert(String sql, List<Map<String, Object>> params, Map<Label, Object> labels)
            throws SQLException {
        Map<String, Object> ret = new LinkedHashMap<>();

        PreparedStatement ps;
        if (labels != null && labels.containsKey(Label.INSERT_GET_ID)) {
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } else {
            ps = connection.prepareStatement(sql);
        }
        try {
            // 数据处理
            for (int i = 0; i < params.size(); i++) {
                Map<String, Object> param = params.get(i);
                String type = param.get("type").toString();
                Object value = param.get("value");
                switch (type) {
                    case "Integer": ps.setInt(i, (Integer) value); break;
                    case "String": ps.setString(i, (String) value); break;
                }
            }
            // 执行
            int count = ps.executeUpdate();
            // 获取自增id
            if (labels != null && labels.containsKey(Label.INSERT_GET_ID)) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    List<String> fieldNameArr = prepareField(generatedKeys);
                    if (!fieldNameArr.isEmpty()) {
                        generatedKeys.next();
                        for (int fi = 1; fi <= generatedKeys.getRow(); fi++) {
                            ret.put(fieldNameArr.get(fi - 1), generatedKeys.getObject(fi));
                        }
                    }
                }
            }
            ret.put("__orm_count", count);
            return ret;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ps.close();
        }
    }

    public Integer update(String sql)
            throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    public Integer delete(String sql)
            throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    public List<Map<String, Object>> select(String sql)
            throws SQLException {
        List<Map<String, Object>> rows = new LinkedList<>();
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            List<String> fieldNameArr = prepareField(resultSet);
            // 数据组装
            while (resultSet.next()) {
                Map<String, Object> item = new LinkedHashMap<>();
                for (int fi = 0; fi < fieldNameArr.size(); fi++) {
                    item.put(fieldNameArr.get(fi), resultSet.getObject(fi + 1));
                }
                rows.add(item);
            }
        }
        return rows;
    }

    private static List<String> prepareField(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        List<String> fieldNameArr = new LinkedList<>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            fieldNameArr.add(metaData.getColumnLabel(i));
        }
        return fieldNameArr;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
