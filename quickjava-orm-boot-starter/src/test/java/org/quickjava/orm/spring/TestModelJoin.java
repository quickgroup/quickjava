/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.orm.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.contain.IPagination;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.spring.domain.entity.SsoApp;
import org.quickjava.orm.spring.domain.entity.SsoAppFavoriteModel;
import org.quickjava.orm.spring.domain.entity.SsoAppLatest;
import org.quickjava.orm.wrapper.ModelWrapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModelJoin {

    @Test
    public void test1()
    {
        Long startTime = TimeUtils.getNanoTime();
        IPagination<SsoAppFavoriteModel> pagination = new ModelWrapper<>(SsoAppFavoriteModel.class)
                // TODO::关联表
                // # 与主表一对一关联join
                .leftJoin(SsoAppFavoriteModel::getAppId, SsoApp.class, SsoAppFavoriteModel::getApp, SsoApp::getAppId)
//                .leftJoin(SsoApp.class, "app", SsoApp::getAppId, SsoAppFavoriteModel::getAppId)   // 等价上面调用
                // ## 与主表一对一关联join，并加载数据
                .leftJoin2(SsoAppFavoriteModel::getAppId, SsoApp.class, "app2", SsoApp::getAppId, SsoAppFavoriteModel::getApp)
//                .leftJoin2(SsoApp.class, "app", SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)   // 等价上面调用
                // ## 与主表一对一关联join，字符串别名
                .leftJoin(SsoAppFavoriteModel::getAppId, SsoApp.class, "app3", SsoApp::getAppId)
                // ## 与主表一对一关联join，复合条件查询
                .leftJoin(SsoApp.class, "app4", query -> query
                        .eq(SsoAppFavoriteModel::getAppId, SsoApp::getAppId)
                        .left()
                            .isNotNull(SsoAppFavoriteModel::getAppId)
                        .right()
                            .eq(SsoApp::getOpen, 1)
                            .isNotNull(SsoApp::getName)
                )
                // ## 与主表一对一关联join，复合条件查询
                .leftJoin(SsoApp.class, "app5", query -> query
                        .eq(SsoAppFavoriteModel::getAppId, SsoApp::getAppId)
                        .and().eq(SsoAppFavoriteModel::getAppId, SsoApp::getAppId)
                        .or().eq(SsoAppFavoriteModel::getAppId, SsoApp::getAppId)
                        .rightField(SsoApp::getAppId)
                )
                // 关联其他表
                .leftJoin(SsoAppFavoriteModel::getAppId, SsoAppLatest.class, SsoAppLatest::getAppId)
                // 关联其他表
                .leftJoin(SsoAppFavoriteModel::getAppId, SsoAppLatest.class, SsoAppFavoriteModel::getTestAppInfo, SsoAppLatest::getAppId)
//                .leftJoin(SsoAppLatest.class, "testAppInfo", SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)       // 等价上面

                // 通过中间表关联
//                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId)
//                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoApp::getAppId, SsoAppFavoriteModel::getTestAppInfo)

                // TODO::查询条件
                // 主表查询条件
                .eq(SsoAppFavoriteModel::getUserId, 1)
                .neq(SsoAppFavoriteModel::getUserId, 0)
                // 关联表查询字段
                //      1. 自动识别表名
                .eq(SsoApp.class, SsoApp::getAppId, 1)
                //      2. 指定父属性名为表名
                .eq(SsoApp.class, SsoAppFavoriteModel::getApp, SsoApp::getAppId, 1)
//                .eq(SsoApp.class, "app", SsoApp::getAppId, 1)     // 等价上面调用
                .eq(SsoApp.class, SsoAppFavoriteModel::getTestAppInfo, SsoApp::getAppId, 1)
//                .eq(SsoApp.class, "testAppInfo", SsoApp::getAppId, 1)     // 等价上面调用
                //      3. 字符串表名
                .eq(SsoApp.class, "app3", SsoApp::getAppId, 1)
                .eq(SsoApp.class, "app4", SsoApp::getAppId, 1)
                // TODO::排序字段
                .order(SsoAppFavoriteModel::getCreateTime, true)
                .order(SsoApp.class, SsoApp::getAppId)
                .order(SsoApp.class, SsoApp::getAppId, true)
                .order(SsoApp.class, SsoApp::getAppId, OrderByType.DESC)

                // TODO::asd
                .field(SsoApp.class, SsoApp::getAppId, SsoApp::getName)

                // 分页查询
                .pagination();

        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

    @Test
    public void test2()
    {
        Long startTime = TimeUtils.getNanoTime();
        List<SsoAppFavoriteModel> favorites = new ModelWrapper<>(SsoAppFavoriteModel.class)
                // 一对一关联
                .leftJoin(SsoAppFavoriteModel::getAppId, SsoApp.class, SsoAppFavoriteModel::getApp, SsoApp::getAppId)
                // 一对一关联 并把关联表数据加载到app属性上
                .leftJoin2(SsoAppFavoriteModel::getAppId, SsoApp.class, SsoAppFavoriteModel::getApp, SsoApp::getAppId, SsoAppFavoriteModel::getApp)
                .select();
        System.out.println("leftJoin return=" + favorites);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

    @Test
    public void testWithFunction()
    {
        Long startTime = TimeUtils.getNanoTime();
        List<SsoAppFavoriteModel> favorites = new ModelWrapper<>(SsoAppFavoriteModel.class)
                .with(SsoAppFavoriteModel::getApp)
                .select();
        System.out.println("with return=" + favorites);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

    @Test
    public void testWithString()
    {
        Long startTime = TimeUtils.getNanoTime();
        List<SsoAppFavoriteModel> favorites = new ModelWrapper<>(SsoAppFavoriteModel.class)
                .with("app")
                .select();
        System.out.println("with return=" + favorites);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

}
