package file.entity;

/**
 * @author sei
 * @description 内存中存储的IndexMap类的值类型，建造者模式
 * @date 17:13 2020/3/28
 */
public class IndexFileEntry {
    /**
     * 时间戳
     */
    private final long tstamp;
    /**
     * 键对应的BucketId
     */
    private final long bucketId;
    /**
     * 值的大小
     */
    private final long valueSize;
    /**
     * 文件偏移量
     */
    private final long offset;


    private IndexFileEntry(indexFileEntryBuilder builder){
        tstamp = builder.tstamp;
        bucketId = builder.bucketId;
        valueSize = builder.valueSize;
        offset = builder.offset;
    }

    public static class indexFileEntryBuilder{
        private long tstamp;

        private long bucketId;

        private long valueSize;

        private long offset;

        public indexFileEntryBuilder setTstamp(long tstamp) {
            this.tstamp = tstamp;
            return this;
        }

        public indexFileEntryBuilder setBucketId(long bucketId) {
            this.bucketId = bucketId;
            return this;
        }

        public indexFileEntryBuilder setValueSize(long valueSize) {
            this.valueSize = valueSize;
            return this;
        }

        public indexFileEntryBuilder setOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public IndexFileEntry build(){
            return new IndexFileEntry(this);
        }
    }

    public static indexFileEntryBuilder builder(){
        return new indexFileEntryBuilder();
    }
}
