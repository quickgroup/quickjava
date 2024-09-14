package org.quickjava.orm.spring.loader;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.contain.DatabaseMeta;
import org.quickjava.orm.loader.ORMContextPort;
import org.quickjava.orm.model.IModel;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.spring.domain.QuickJavaOrmProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class SpringLoader implements InitializingBean, ORMContextPort {

    private static final Logger logger = LoggerFactory.getLogger(SpringLoader.class);

    private static ApplicationContext applicationContext;

    private static SpringLoader instance;

    @Autowired
    private Environment environment;
    @Autowired
    private QuickJavaOrmProps ormProps;
    @Autowired
    private DataSource druidDataSource;

    private DatabaseMeta.DBType dbType;

    private DatabaseMeta dbConfig;

    public SpringLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.instance = this;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static  <T> T getBean(Class<T> var1) {
        try {
            return applicationContext.getBean(var1);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // appClassLoader
        ORMContext.setClassLoader(Model.class.getClassLoader());
        // 配置数据库来源
        ORMContext.setContextPort(this);
        // mybatis-plus字段支持
        MyBatisPlusConfigure.init(this);
        // 加载模型
        this.loadModels();
        // 更换classLoader
        ORMContext.setClassLoader(applicationContext.getClassLoader());
    }

    public DatabaseMeta.DBType getConnectionType() {
        synchronized (SpringLoader.class) {
            if (dbType == null) {
                try {
                    Connection connection = druidDataSource.getConnection();
                    dbType = DatabaseMeta.parseTypeFromConnection(connection);
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return dbType;
    }

    public DatabaseMeta getDatabaseMeta() {
        if (dbConfig == null) {
            dbConfig = new DatabaseMeta("SpringBoot " + SpringVersion.getVersion(), getConnectionType());
            dbConfig.setUnderline(true);
            dbConfig.setCamelCase(true);
        }
        return dbConfig;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return druidDataSource.getConnection();
    }

    public Environment getEnvironment() {
        return environment;
    }

    public QuickJavaOrmProps getOrmProps() {
        return ormProps;
    }

    private void loadModels() {
        String typeAliasesPackage = getOrmProps().getTypeAliasesPackage();
        logger.debug("Load model entities, in package: " + typeAliasesPackage);
        if (typeAliasesPackage == null) {
            logger.warn("model entities package path error");
            return;
        }

        List<Model> models = new LinkedList<>();
        ClassPathScanningCandidateComponentProvider scanner2 = new ClassPathScanningCandidateComponentProvider(false);
        scanner2.addIncludeFilter((m, f) -> {
            try {
                Class<?> clazz = Class.forName(m.getClassMetadata().getClassName());
                if (Model.class.isAssignableFrom(clazz)) {
                    logger.debug("Load model entity: {}", clazz.getName());
                    Model model = (Model) clazz.newInstance();
                    models.add(model);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return false;
        });
        logger.debug("Load completed {} models.", models.size());
        // 扫描指定包
        scanner2.findCandidateComponents(typeAliasesPackage);
        // 初始化关联关系
        Method method = ReflectUtil.getMethod(Model.class, "initRelation", IModel.class);
        for (Model model : models) {
            ReflectUtil.invokeStatic(method, model);
        }
        logger.debug("Associative method loading complete.");
    }
}
