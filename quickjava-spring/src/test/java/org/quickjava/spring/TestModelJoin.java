/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.query.enums.OrderByType;
import org.quickjava.orm.wrapper.ModelWrapper;
import org.quickjava.spring.entity.SsoApp;
import org.quickjava.spring.entity.SsoAppLatest;
import org.quickjava.spring.entity.SsoAppFavoriteModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModelJoin {

    @Test
    public void test1()
    {
        Long startTime = TimeUtils.getNanoTime();
        Pagination<SsoAppFavoriteModel> pagination = new ModelWrapper<>(SsoAppFavoriteModel.class)
                // TODO::关联表
                // 与主表一对一关联join
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, "aliasApp01")
                // 与主表一对一关联join，复合条件查询
                .leftJoin(SsoApp.class, "app3", whereLeft -> whereLeft
                        .eq(SsoApp::getAppId, SsoAppFavoriteModel.class, SsoAppFavoriteModel::getAppId)
                        .eq(SsoApp::getOpen, 1)
                        .isNotNull(SsoApp::getName)
                )
                // 与主表一对一关联join，指定数据加载到主实体指定属性
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp2)
                // 关联其他表
                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)
                // 关联其他表
                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getTestAppInfo)

                // TODO::查询条件
                // 主表查询条件
                .eq(SsoAppFavoriteModel::getUserId, 1)
                .neq(SsoAppFavoriteModel::getUserId, 0)
                // 关联表查询字段
                //      1. 自动识别表名
                .eq(SsoApp.class, SsoApp::getAppId, 1)
                //      2. 指定父属性名为表名
                .eq(SsoAppFavoriteModel::getApp, SsoApp.class, SsoApp::getAppId, 1)
                .eq(SsoAppFavoriteModel::getTestAppInfo, SsoApp.class, SsoApp::getAppId, 1)
                //      3. 字符串表名
                .eq("aliasApp01", SsoApp::getAppId, 1)
                .eq("app3", SsoApp::getAppId, 1)

                // TODO::排序字段
                .order(SsoAppFavoriteModel::getCreateTime, true)
                .order(SsoApp.class, SsoApp::getAppId)
                .order(SsoApp.class, SsoApp::getAppId, true)
                .order(SsoApp.class, SsoApp::getAppId, OrderByType.DESC)

                // 分页查询
                .pagination();

        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

    @Test
    public void test2()
    {
        Long startTime = TimeUtils.getNanoTime();
        Pagination<SsoAppFavoriteModel> pagination = new ModelWrapper<>(SsoAppFavoriteModel.class)
                // 与主表一对一关联join
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)
                // 与主表一对一关联join，复合条件查询
                .leftJoin(SsoApp.class, whereLeft -> whereLeft
                        .eq(SsoApp::getAppId, SsoAppFavoriteModel.class, SsoAppFavoriteModel::getAppId)
                        .eq(SsoApp::getOpen, 1))
                // 与主表一对一关联join，指定数据加载到主实体指定属性
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp2)
                // 关联其他表
                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)
                .pagination();
        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

}
