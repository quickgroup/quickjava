package org.quickjava.orm.spring.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 应用使用记录
 */
@TableName("sso_app_latest")
public class SsoAppLatest extends BaseSimpleEntity
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

    public SsoAppLatest() {
    }

    public SsoAppLatest(Long userId, Long appId) {
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
}
