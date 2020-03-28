package file.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author sei
 * @description Bucket存储的条目实体类，建造者模式
 * @date 17:13 2020/3/28
 */
public class BucketEntry {
    /**
     * 时间戳
     */
    private final long tstamp;
    /**
     * 键大小
     */
    private final int keySize;
    /**
     * 值大小
     */
    private final int valueSize;
    /**
     * 键
     */
    private final byte[] key;
    /**
     * 值
     */
    private final byte[] value;

    private BucketEntry(BucketEntryBuilder builder){
        tstamp = builder.tstamp;
        keySize = builder.keySize;
        valueSize = builder.valueSize;
        key = builder.key;
        value = builder.value;
    }

    public static class BucketEntryBuilder {
        private final long tstamp;

        private int keySize;

        private int valueSize;

        private byte[] key;

        private byte[] value;

        public BucketEntryBuilder(){
            tstamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        }

        public BucketEntryBuilder setKey(byte[] key) {
            this.key = key;
            this.keySize = key.length;
            return this;
        }

        public BucketEntryBuilder setValue(byte[] value) {
            this.value = value;
            this.valueSize = value.length;
            return this;
        }

        public BucketEntry build(){
            return new BucketEntry(this);
        }
    }

    public static BucketEntryBuilder builder(){
        return new BucketEntryBuilder();
    }

}
