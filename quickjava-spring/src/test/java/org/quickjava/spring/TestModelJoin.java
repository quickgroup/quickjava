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
//                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp)
                .leftJoin(SsoApp.class, whereLeft -> {
                    whereLeft.eq(SsoApp::getAppId, SsoAppFavoriteModel.class, SsoAppFavoriteModel::getAppId)
                            .eq(SsoApp::getOpen, 1);
                })
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavoriteModel::getAppId, SsoAppFavoriteModel::getApp2)
                .leftJoin(SsoAppLatest.class, SsoAppLatest::getAppId, SsoAppFavoriteModel::getAppId)
                .pagination();
        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

}
