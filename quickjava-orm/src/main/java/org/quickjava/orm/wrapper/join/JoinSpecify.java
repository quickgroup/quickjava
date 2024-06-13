package org.quickjava.orm.wrapper.join;

import org.quickjava.orm.model.Model;

import java.util.List;

public interface JoinSpecify<Left extends Model, Right extends Model>
{
    public Class<Left> getLeft();

    public String getLeftAlias();

    public JoinSpecify<Left, Right> setLeftAlias(String leftAlias);

    public Class<Right> getRight();

    public String getRightAlias();

    public String getLoadDataFieldName();

    public List<JoinSpecifyAbs.Item<?, ?>> getOnList();

}
