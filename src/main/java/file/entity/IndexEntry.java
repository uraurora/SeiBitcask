package file.entity;

import java.util.Objects;

/**
 * @author sei
 * @description 内存中存储的IndexMap类的值类型，建造者模式
 * @date 17:13 2020/3/28
 */
public class IndexEntry {
    /**
     * 时间戳
     */
    private final long tstamp;
    /**
     * 键对应的BucketId
     */
    private final int bucketId;
    /**
     * 值的大小
     */
    private final int valueSize;
    /**
     * 文件偏移量
     */
    private final long offset;


    private IndexEntry(IndexEntryBuilder builder){
        tstamp = builder.tstamp;
        bucketId = builder.bucketId;
        valueSize = builder.valueSize;
        offset = builder.offset;
    }

    public static class IndexEntryBuilder {
        private long tstamp;

        private int bucketId;

        private int valueSize;

        private long offset;

        public IndexEntryBuilder setTstamp(long tstamp) {
            this.tstamp = tstamp;
            return this;
        }

        public IndexEntryBuilder setBucketId(int bucketId) {
            this.bucketId = bucketId;
            return this;
        }

        public IndexEntryBuilder setValueSize(int valueSize) {
            this.valueSize = valueSize;
            return this;
        }

        public IndexEntryBuilder setOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public IndexEntry build(){
            return new IndexEntry(this);
        }
    }

    public static IndexEntryBuilder builder(){
        return new IndexEntryBuilder();
    }

    public long getTstamp() {
        return tstamp;
    }

    public int getBucketId() {
        return bucketId;
    }

    public int getValueSize() {
        return valueSize;
    }

    public long getOffset() {
        return offset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IndexEntry that = (IndexEntry) o;
        return tstamp == that.tstamp &&
                bucketId == that.bucketId &&
                valueSize == that.valueSize &&
                offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tstamp, bucketId, valueSize, offset);
    }

    @Override
    public String toString() {
        return "IndexEntry{" +
                "tstamp=" + tstamp +
                ", bucketId=" + bucketId +
                ", valueSize=" + valueSize +
                ", offset=" + offset +
                '}';
    }
}
