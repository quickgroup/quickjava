/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.contain.Pagination;
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
                // 与主表一对一关联join
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)
                // 与主表一对一关联join，复合条件查询
                .leftJoin(SsoApp.class, "app3", whereLeft -> whereLeft
                        .eq(SsoApp::getAppId, SsoAppFavoriteModel.class, SsoAppFavoriteModel::getAppId)
                        .eq(SsoApp::getOpen, 1))
                // 与主表一对一关联join，指定数据加载到主实体指定属性
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp2)
                // 关联其他表
                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)
                // 主表查询条件
//                .eq(SsoAppFavoriteModel::getUserId, 1)
//                .neq(SsoAppFavoriteModel::getUserId, 0)
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
