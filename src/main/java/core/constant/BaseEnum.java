package core.constant;

public interface BaseEnum<L, V> {
    /**
     * 获取枚举值
     * @return V
     */
    V getVal();

    /**
     * 获取标签
     * @return L
     */
    L getLabel();
}
