package org.quickjava.orm.wrapper.join;

import java.util.List;

public interface JoinSpecify<Left, Right>
{
    public Class<Left> getLeft();

    public JoinSpecify<Left, Right> setLeft(Class<?> left);

    public String getLeftAlias();

    public JoinSpecify<Left, Right> setLeftAlias(String leftAlias);

    public Class<Right> getRight();

    public JoinSpecify<Left, Right> setRight(Class<?> right);

    public String getRightAlias();

    public String getLoadDataFieldName();

    public List<JoinSpecifyAbs.Item<?, ?>> getOnList();

}
