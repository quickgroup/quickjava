package org.quickjava.spring.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 应用收藏
 */
@TableName("sso_app_favorite")
public class SsoAppFavoriteModel extends BaseSimpleEntity
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

    // 关联应用信息
    @TableField(exist = false)
    private SsoApp app2;

    @TableField(exist = false)
    private SsoAppFavoriteModel sysAppFavorite;

    public SsoAppFavoriteModel() {
    }

    public SsoAppFavoriteModel(Long userId, Long appId) {
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

    public SsoApp getApp2() {
        return app2;
    }

    public void setApp2(SsoApp app2) {
        this.app2 = app2;
    }

    public SsoAppFavoriteModel getSysAppFavorite() {
        return sysAppFavorite;
    }

    public void setSysAppFavorite(SsoAppFavoriteModel sysAppFavorite) {
        this.sysAppFavorite = sysAppFavorite;
    }
}
