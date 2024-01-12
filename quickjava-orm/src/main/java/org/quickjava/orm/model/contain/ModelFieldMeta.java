package org.quickjava.orm.model.contain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import org.quickjava.common.utils.ComUtil;
import org.quickjava.orm.model.annotation.ModelField;
import org.quickjava.orm.model.annotation.ModelId;
import org.quickjava.orm.model.enums.ModelFieldFill;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModelFieldMeta {

    private String name;

    // 对应类
    private Class<?> clazz;

    private Field field;

    // 关联字段
    private Object relationWay;

    private Method setter;

    private Method getter;

    private Method method;

    // quickjava-orm注解
    private ModelField modelField;
    private ModelId modelId;

    // mybatis-plus注解
    private TableField tableField;
    private TableId tableId;
    private TableLogic tableLogic;

    public ModelFieldMeta() {
    }

    public ModelFieldMeta(Field field) {
        this.name = field.getName();
        this.clazz = field.getType();
        this.field = field;
        this.modelField = field.getAnnotation(ModelField.class);
        this.modelId = field.getAnnotation(ModelId.class);
        this.tableField = field.getAnnotation(TableField.class);
        this.tableId = field.getAnnotation(TableId.class);
        this.tableLogic = field.getAnnotation(TableLogic.class);
    }

    public ModelFieldMeta(Field field, Object relationWay, Method setter, Method getter) {
        this(field);
        this.relationWay = relationWay;
        this.setter = setter;
        this.getter = getter;
    }

    public String getName() {
        if (modelField != null) {
            return "".equals(modelField.name()) ? name:  modelField.name();
        } else if (tableField != null) {
            return "".equals(tableField.value()) ? name:  tableField.value();
        }
        return name;
    }

    public String name() {
        return getName();
    }

    public String getNameCamelCase() {
        return ComUtil.toCamelCase(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Object getRelationWay() {
        return relationWay;
    }

    public void setRelationWay(Object relationWay) {
        this.relationWay = relationWay;
    }

    public Method getSetter() {
        return setter;
    }

    public void setSetter(Method setter) {
        this.setter = setter;
    }

    public Method getGetter() {
        return getter;
    }

    public void setGetter(Method getter) {
        this.getter = getter;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ModelField getModelField() {
        return modelField;
    }

    public void setModelField(ModelField modelField) {
        this.modelField = modelField;
    }

    public ModelId getModelId() {
        return modelId;
    }

    public void setModelId(ModelId modelId) {
        this.modelId = modelId;
    }

    public TableId getTableId() {
        return tableId;
    }

    public void setTableId(TableId tableId) {
        this.tableId = tableId;
    }

    public TableField getTableField() {
        return tableField;
    }

    public void setTableField(TableField tableField) {
        this.tableField = tableField;
    }

    public boolean isInsertFill() {
        if (modelField != null) {
            return modelField.insertFill() != ModelFieldFill.NULL;
        } else if (tableField != null) {
            return tableField.fill() == FieldFill.INSERT || tableField.fill() == FieldFill.INSERT_UPDATE;
        }
        return false;
    }

    public boolean isUpdateFill() {
        if (modelField != null) {
            return modelField.updateFill() != ModelFieldFill.NULL;
        } else if (tableField != null) {
            return tableField.fill() == FieldFill.UPDATE || tableField.fill() == FieldFill.INSERT_UPDATE;
        }
        return false;
    }

    public boolean isSoftDelete() {
        if (modelField != null) {
            return modelField.softDelete();
        } else if (tableLogic != null) {
            return true;
        }
        return false;
    }

    /**
     * 是关联属性
     * @return 是关联属性
     */
    public boolean isRelation() {
        return getRelationWay() != null;
    }

    /**
     * 是否是数据库表字段
     * @return 属性是否在表中存在
     */
    public boolean isExist() {
        if (modelField != null) {
            return modelField.exist();
        } else if (tableField != null) {
            return tableField.exist();
        }
        return true;
    }

    @Override
    public String toString() {
        return "ModelFieldO{" +
                "name='" + name + '\'' +
                ", clazz=" + clazz +
                ", field=" + field +
                ", way=" + relationWay +
                ", setter=" + setter +
                ", getter=" + getter +
                ", method=" + method +
                ", modelField=" + modelField +
                ", modelId=" + modelId +
                ", tableId=" + tableId +
                ", tableField=" + tableField +
                '}';
    }
}
