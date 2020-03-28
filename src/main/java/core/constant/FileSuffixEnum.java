package core.constant;
/**
 * @author sei
 * @description 持久化文件的后缀
 * @date 18:31 2020/3/28
 */
public enum FileSuffixEnum {

    BUCKET(".bucket"),

    INDEX(".index"),

    HINT(".hint"),
    ;

    private final String val;

    FileSuffixEnum(String val) {
        this.val = val;
    }

    public String getVal(){
        return this.val;
    }
}
