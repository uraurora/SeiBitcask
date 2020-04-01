package core.constant;
/**
 * @author sei
 * @description 持久化文件的后缀
 * @date 18:31 2020/3/28
 */
public enum FileConstEnum {

    BUCKET_PREFIX("bucket"),

    INDEX_PREFIX("index"),

    HINT_PREFIX("hint"),

    DATA_SUFFIX(".dat")
    ;

    private final String val;

    FileConstEnum(String val) {
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }
}
