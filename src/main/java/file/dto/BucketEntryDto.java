package file.dto;


public class BucketEntryDto {
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
    /**
     * 文件偏移
     */
    private final long offset;

    private BucketEntryDto(BucketEntryDtoBuilder builder){
        tstamp = builder.tstamp;
        keySize = builder.keySize;
        valueSize = builder.valueSize;
        key = builder.key;
        value = builder.value;
        offset = builder.offset;
    }

    public static class BucketEntryDtoBuilder {
        private long tstamp;

        private int keySize;

        private int valueSize;

        private byte[] key;

        private byte[] value;

        private long offset;

        public BucketEntryDtoBuilder(){}

        public BucketEntryDtoBuilder setKey(byte[] key) {
            this.key = key;
            this.keySize = key.length;
            return this;
        }

        public BucketEntryDtoBuilder setValue(byte[] value) {
            this.value = value;
            this.valueSize = value.length;
            return this;
        }

        public BucketEntryDtoBuilder setTstamp(long tstamp) {
            this.tstamp = tstamp;
            return this;
        }

        public BucketEntryDtoBuilder setOffset(long offset) {
            this.offset = offset;
            return this;
        }

        public BucketEntryDto build(){
            return new BucketEntryDto(this);
        }
    }

    public static BucketEntryDtoBuilder builder(){
        return new BucketEntryDtoBuilder();
    }

    public long getTstamp() {
        return tstamp;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getValueSize() {
        return valueSize;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }

    public long getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "BucketEntryDto{" +
                "tstamp=" + tstamp +
                ", keySize=" + keySize +
                ", valueSize=" + valueSize +
                ", offset=" + offset +
                '}';
    }
}
