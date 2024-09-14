/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.contain.IPagination;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.wrapper.ModelWrapper;
import org.quickjava.spring.domain.entity.SysApp;
import org.quickjava.spring.domain.entity.SysAppLatest;
import org.quickjava.spring.domain.entity.SysAppFavoriteModel;
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
        IPagination<SysAppFavoriteModel> pagination = new ModelWrapper<>(SysAppFavoriteModel.class)
                // TODO::关联表
                // # 与主表一对一关联join
                .leftJoin(SysAppFavoriteModel::getAppId, SysApp.class, SysAppFavoriteModel::getApp, SysApp::getAppId)
//                .leftJoin(SsoApp.class, "app", SsoApp::getAppId, SsoAppFavoriteModel::getAppId)   // 等价上面调用
                // ## 与主表一对一关联join，并加载数据
                .leftJoin2(SysAppFavoriteModel::getAppId, SysApp.class, "app2", SysApp::getAppId, SysAppFavoriteModel::getApp)
//                .leftJoin2(SsoApp.class, "app", SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)   // 等价上面调用
                // ## 与主表一对一关联join，字符串别名
                .leftJoin(SysAppFavoriteModel::getAppId, SysApp.class, "aliasApp01", SysApp::getAppId)
                // ## 与主表一对一关联join，复合条件查询
                .leftJoin(SysApp.class, "app3", query -> query
                        .eq(SysAppFavoriteModel::getAppId, SysApp::getAppId)
                        .left()
                            .isNull(SysAppFavoriteModel::getAppId)
                        .right()
                            .eq(SysApp::getOpen, 1)
                            .isNotNull(SysApp::getName)
                            .isNull(SysApp::getAppSecret)
                )
                // ## 与主表一对一关联join，复合条件查询
                .leftJoin(SysApp.class, "app4", query -> query
                        .eq(SysAppFavoriteModel::getAppId, SysApp::getAppId)
                        .and().eq(SysAppFavoriteModel::getAppId, SysApp::getAppId)
                        .or().eq(SysAppFavoriteModel::getAppId, SysApp::getAppId)
                        .rightField(SysApp::getAppId)
                )
                // 关联其他表
                .leftJoin(SysAppFavoriteModel::getAppId, SysAppLatest.class, SysAppLatest::getAppId)
                // 关联其他表
                .leftJoin(SysAppFavoriteModel::getAppId, SysAppLatest.class, SysAppFavoriteModel::getTestAppInfo, SysAppLatest::getAppId)
//                .leftJoin(SsoAppLatest.class, "testAppInfo", SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)       // 等价上面

                // 通过中间表关联
//                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId)
//                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoApp::getAppId, SsoAppFavoriteModel::getTestAppInfo)

                // TODO::查询条件
                // 主表查询条件
                .eq(SysAppFavoriteModel::getUserId, 1)
                .neq(SysAppFavoriteModel::getUserId, 0)
                // 关联表查询字段
                //      1. 自动识别表名
                .eq(SysApp.class, SysApp::getAppId, 1)
                //      2. 指定父属性名为表名
                .eq(SysApp.class, SysAppFavoriteModel::getApp, SysApp::getAppId, 1)
//                .eq(SsoApp.class, "app", SsoApp::getAppId, 1)     // 等价上面调用
                .eq(SysApp.class, SysAppFavoriteModel::getTestAppInfo, SysApp::getAppId, 1)
//                .eq(SsoApp.class, "testAppInfo", SsoApp::getAppId, 1)     // 等价上面调用
                //      3. 字符串表名
                .eq(SysApp.class, "aliasApp01", SysApp::getAppId, 1)
                .eq(SysApp.class, "app3", SysApp::getAppId, 1)
                // TODO::排序字段
                .order(SysAppFavoriteModel::getCreateTime, true)
                .order(SysApp.class, SysApp::getAppId)
                .order(SysApp.class, SysApp::getAppId, true)
                .order(SysApp.class, SysApp::getAppId, OrderByType.DESC)

                // TODO::asd
                .field(SysApp.class, SysApp::getAppId, SysApp::getName)

                // 分页查询
                .pagination();

        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

    @Test
    public void test2()
    {
        Long startTime = TimeUtils.getNanoTime();
        List<SysAppFavoriteModel> favorites = new ModelWrapper<>(SysAppFavoriteModel.class)
                // 一对一关联
                .leftJoin(SysAppFavoriteModel::getAppId, SysApp.class, SysAppFavoriteModel::getApp, SysApp::getAppId)
                // 一对一关联 并把关联表数据加载到app属性上
                .leftJoin2(SysAppFavoriteModel::getAppId, SysApp.class, SysAppFavoriteModel::getApp, SysApp::getAppId, SysAppFavoriteModel::getApp)
                .select();
        System.out.println("leftJoin return=" + favorites);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

}
