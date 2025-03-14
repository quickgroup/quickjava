package org.quickjava.orm.loader;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.contain.DatabaseConfig;
import org.quickjava.orm.model.IModel;
import org.quickjava.orm.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
public class SpringLoader implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SpringLoader.class);

    private static ApplicationContext applicationContext;

    public static SpringLoader instance;

//    public static MybatisProperties mybatisProperties;

//    public static MybatisPlusProperties mybatisPlusProperties;

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource druidDataSource;

    private DatabaseConfig.DBType dbType;

    private DatabaseConfig dbConfig;

    public SpringLoader(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.instance = this;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // appClassLoader
        ORMContext.setClassLoader(Model.class.getClassLoader());
        // 加载模型
        this.loadModel();
        // 更换classLoader
        ORMContext.setClassLoader(applicationContext.getClassLoader());
    }

    public DataSource getDataSource() {
        return druidDataSource;
    }

    public DatabaseConfig.DBType getConnectionType() {
        if (dbType == null) {
            try (Connection connection = getDataSource().getConnection()) {
                dbType = DatabaseConfig.parseTypeFromConnection(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return dbType;
    }

    public DatabaseConfig getConfig() {
        if (dbConfig == null) {
            dbConfig = new DatabaseConfig(DatabaseConfig.DBSubject.SPRING, getConnectionType());
        }
        return dbConfig;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private void loadModel() {
        String typeAliasesPackage = environment.getProperty("mybatis.type-aliases-package");
        if (ObjectUtil.isEmpty(typeAliasesPackage)) {
            typeAliasesPackage = environment.getProperty("mybatis-plus.type-aliases-package");
        }
        if (ObjectUtil.isEmpty(typeAliasesPackage)) {
            typeAliasesPackage = environment.getProperty("spring.component-scan.base-package");
        }
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
//                    logger.debug("Load model entity: {}", clazz.getName());
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
