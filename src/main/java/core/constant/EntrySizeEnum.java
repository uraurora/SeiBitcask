package core.constant;

public enum EntrySizeEnum {
    // TODO: 使用配置表读取实现，不应该使用枚举写死
    KEY_MAX_SIZE(32),

    VAL_MAX_SIZE(1024*1024*128),

    ;

    private final int val;

    EntrySizeEnum(int val) {
        this.val = val;
    }

    public int getVal(){
        return this.val;
    }
}
