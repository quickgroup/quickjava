package org.quickjava.orm.wrapper;

import org.quickjava.orm.enums.LogicType;
import org.quickjava.orm.model.Model;
import org.quickjava.orm.model.ModelHelper;
import org.quickjava.orm.query.QuerySetHelper;
import org.quickjava.orm.query.build.Where;
import org.quickjava.orm.query.build.WhereAnd;
import org.quickjava.orm.query.enums.Operator;

import java.util.List;

public class MFunctionCollector<M extends Model, MC extends MFunction<M, ?>>
        implements AbstractWhere<MFunctionCollector<M, MC>, M, MC> {

    private Class<M> modelClazz;

    private String alias;

    private List<Where> whereList;

    public MFunctionCollector(Class<M> modelClazz) {
        this.modelClazz = modelClazz;
    }

    public MFunctionCollector(Class<M> modelClazz, String alias) {
        this.modelClazz = modelClazz;
        this.alias = alias;
    }

    public MFunctionCollector<M, MC> where(Where where) {
        whereList = QuerySetHelper.initList(whereList);
        whereList.add(where);
        return chain();
    }

    public MFunctionCollector<M, MC> call(MFunctionCollector.Closure closure) {
        closure.call(this);
        return chain();
    }

    public Class<M> getModelClazz() {
        return modelClazz;
    }

    public void setModelClazz(Class<M> modelClazz) {
        this.modelClazz = modelClazz;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public List<Where> getWhereList() {
        return whereList;
    }

    public void setWhereList(List<Where> whereList) {
        this.whereList = whereList;
    }

    public interface Closure {
        <M extends Model, MC extends MFunction<M, ?>> void call(MFunctionCollector<M, MC> collector);
    }
}
