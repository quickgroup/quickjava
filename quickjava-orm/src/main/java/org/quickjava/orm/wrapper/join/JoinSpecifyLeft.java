package org.quickjava.orm.wrapper.join;

/**
 * join条件
 * 左右两边对象类确定
 * 右表确定
 */
public class JoinSpecifyLeft<Left, Right> extends JoinSpecifyAbs<JoinSpecifyLeft<Left, Right>, Left, Right>
{

    public JoinSpecifyLeft(Class<Right> right) {
        super(null, right);
    }

    public JoinSpecifyLeft(Class<Right> right, String rightAlias) {
        super(null, "", right, rightAlias);
    }

    public JoinSpecifyLeft<Left, Right> setRight(Class<Right> right, String rightAlias) {
        this.right = right;
        this.rightAlias = rightAlias;
        return this;
    }

    public JoinSpecifyLeft<Left, Right> setRightAlias(String rightAlias) {
        this.rightAlias = rightAlias;
        return this;
    }

}
