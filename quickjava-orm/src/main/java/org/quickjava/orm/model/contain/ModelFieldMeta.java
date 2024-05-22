package org.quickjava.orm.model.contain;

import org.quickjava.common.utils.ComUtil;
import org.quickjava.orm.ORMContext;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.model.annotation.ModelField;
import org.quickjava.orm.model.annotation.ModelId;
import org.quickjava.orm.model.callback.ModelStrut;
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

    public ModelFieldMeta() {
    }

    public ModelFieldMeta(Field field) {
        this.name = field.getName();
        this.clazz = field.getType();
        this.field = field;
        this.modelField = field.getAnnotation(ModelField.class);
        this.modelId = field.getAnnotation(ModelId.class);
    }

    public ModelFieldMeta(Field field, Object relationWay, Method setter, Method getter) {
        this(field);
        this.relationWay = relationWay;
        this.setter = setter;
        this.getter = getter;
    }

    public String getName() {
        String alias = this.name;
        if (modelField != null) {
            return isEmpty(modelField.value()) ? alias:  modelField.value();
        }
        // 三方支持
        if (ORMContext.getModelStrut() != null) {
            alias = ORMContext.getModelStrut().getTableFieldName(this);
            if (isNotEmpty(alias)) {
                return alias;
            }
        }
        return this.name;
    }

    private boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private boolean isNotEmpty(String str) {
        return !isEmpty(str);
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

    public boolean hasInsertFill() {
        if (modelField != null) {
            return modelField.insertFill() != ModelFieldFill.NULL;
        }
        // 三方支持
        if (modelStrut() != null) {
            Boolean ret = modelStrut().hasInsertFull(this);
            if (ret != null) {
                return ret;
            }
        }
        return false;
    }

    public Object insertFill() {
        if (modelField != null && modelField.insertFill() != ModelFieldFill.NULL) {
            return ModelHelper.fill(field, getModelField().insertFill(), getModelField().insertFillTarget());
        }
        // 三方支持
        if (modelStrut() != null) {
            Boolean ret = modelStrut().hasInsertFull(this);
            if (ret != null) {
                return modelStrut().getInsertFullValue(this);
            }
        }
        return null;
    }

    public boolean hasUpdateFill() {
        if (modelField != null) {
            return modelField.updateFill() != ModelFieldFill.NULL;
        }
        // 三方支持
        if (ORMContext.getModelStrut() != null) {
            return ORMContext.getModelStrut().hasUpdateFull(this);
        }
        return false;
    }

    public Object updateFill() {
        if (modelField != null && modelField.updateFill() != ModelFieldFill.NULL) {
            return ModelHelper.fill(field, modelField.updateFill(), modelField.updateFillTarget());
        }
        // 三方支持
        if (modelStrut() != null) {
            Boolean ret = modelStrut().hasUpdateFull(this);
            if (ret != null) {
                return modelStrut().getUpdateFullValue(this);
            }
        }
        return null;
    }

    public boolean isSoftDelete() {
        if (modelField != null) {
            return modelField.softDelete();
        }
        // 三方支持
        if (modelStrut() != null) {
            return modelStrut().isTableLogic(this);
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
        }
        // 三方支持
        if (ORMContext.getModelStrut() != null) {
            Boolean ret = ORMContext.getModelStrut().tableFieldExist(this);
            if (ret != null) {
                return ret;
            }
        }
        return true;
    }

    public ModelStrut modelStrut() {
        return ORMContext.getModelStrut();
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
                '}';
    }
}
