/*
 * Copyright (c) 2021.
 * More Info to http://www.quickjava.org
 */

package org.quickjava.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.quickjava.common.utils.TimeUtils;
import org.quickjava.orm.contain.Pagination;
import org.quickjava.orm.wrapper.ModelQueryWrapper;
import org.quickjava.spring.entity.SsoApp;
import org.quickjava.spring.entity.SsoAppFavorite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestModelJoin {

    @Test
    public void test1()
    {
        Long startTime = TimeUtils.getNanoTime();
        Pagination<SsoAppFavorite> pagination = new ModelQueryWrapper<SsoAppFavorite>(SsoAppFavorite.class)
                .leftJoin(SsoApp.class, SsoApp::getAppId, SsoAppFavorite::getAppId)
                .pagination();
        System.out.println("leftJoin return=" + pagination);
        System.out.println("耗时=" + TimeUtils.endNanoTime(startTime) + "ms");
    }

}
