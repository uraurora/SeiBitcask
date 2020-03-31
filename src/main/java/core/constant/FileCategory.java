package core.constant;

import java.io.IOException;
import java.io.RandomAccessFile;

public enum FileCategory {

    BOOL{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.writeBoolean((Boolean) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readBoolean();
        }
    },

    BYTE{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.write((Byte) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readByte();
        }
    },

    CHAR{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.writeChar((Character) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readChar();
        }
    },

    INT{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.writeInt((Integer) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readInt();
        }
    },

    LONG{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.writeLong((Long) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readLong();
        }
    },

    DOUBLE{
        @Override
        public <T> void write(RandomAccessFile raf, T obj) throws IOException {
            raf.writeDouble((Double) obj);
        }

        @Override
        public Object read(RandomAccessFile raf) throws IOException {
            return raf.readDouble();
        }
    },

    ;

    public abstract <T> void write(RandomAccessFile raf, T obj) throws IOException;

    public abstract Object read(RandomAccessFile raf) throws IOException;

}
