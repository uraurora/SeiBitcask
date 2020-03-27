package file.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LogFileEntry {

    private final long tstamp;

    private final int keySize;

    private final int valueSize;

    private final byte[] key;

    private final byte[] value;

    private LogFileEntry(LogFileEntryBuilder builder){
        tstamp = builder.tstamp;
        keySize = builder.keySize;
        valueSize = builder.valueSize;
        key = builder.key;
        value = builder.value;
    }

    public static class LogFileEntryBuilder{
        private final long tstamp;

        private int keySize;

        private int valueSize;

        private byte[] key;

        private byte[] value;

        public LogFileEntryBuilder(){
            tstamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        }

        public LogFileEntryBuilder setKey(byte[] key) {
            this.key = key;
            this.keySize = key.length;
            return this;
        }

        public LogFileEntryBuilder setValue(byte[] value) {
            this.value = value;
            this.valueSize = value.length;
            return this;
        }

        public LogFileEntry build(){
            return new LogFileEntry(this);
        }
    }

    public static LogFileEntryBuilder builder(){
        return new LogFileEntryBuilder();
    }

}
