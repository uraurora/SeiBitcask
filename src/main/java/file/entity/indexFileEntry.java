package file.entity;

public class indexFileEntry {

    private final long tstamp;

    private final long fileId;

    private final long valueSize;

    private final long offset;


    private indexFileEntry(indexFileEntryBuilder builder){
        tstamp = builder.tstamp;
        fileId = builder.fileId;
        valueSize = builder.valueSize;
        offset = builder.offset;
    }

    public static class indexFileEntryBuilder{
        private long tstamp;

        private long fileId;

        private long valueSize;

        private long offset;

        public indexFileEntryBuilder setTstamp(long tstamp) {
            this.tstamp = tstamp;
            return this;
        }

        public indexFileEntryBuilder setFileId(long fileId) {
            this.fileId = fileId;
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

        public indexFileEntry build(){
            return new indexFileEntry(this);
        }
    }

    public static indexFileEntryBuilder builder(){
        return new indexFileEntryBuilder();
    }
}
