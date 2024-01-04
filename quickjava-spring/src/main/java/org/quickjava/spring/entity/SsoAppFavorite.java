package org.quickjava.spring.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 应用收藏
 */
public class SsoAppFavorite extends BaseSimpleEntity
{

    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /** userId */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /** appId */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long appId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 关联应用信息
    @TableField(exist = false)
    private SsoApp app;

    public SsoAppFavorite() {
    }

    public SsoAppFavorite(Long userId, Long appId) {
        this.userId = userId;
        this.appId = appId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public SsoApp getApp() {
        return app;
    }

    public void setApp(SsoApp app) {
        this.app = app;
    }
}
