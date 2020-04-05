package file.entity;

import util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class Bucket implements Iterable<BucketEntry>{

    private final File file;

    private Bucket(File file) {
        this.file = file;
    }

    public static Bucket newInstance(File file) {
        return new Bucket(file);
    }

    public void write(BucketEntry entry) {
        FileUtil.writeTail(file, entry.toBytes());
    }

    public byte[] read(IndexEntry indexEntry) {
        return FileUtil.read(file, indexEntry.getOffset() - indexEntry.getValueSize(), indexEntry.getValueSize());
    }

    @Override
    public Iterator<BucketEntry> iterator() {
        return new BucketItr(0L);
    }

    private class BucketItr implements Iterator<BucketEntry>{

        RandomAccessFile raf;

        final long end = file.length();

        long offset;

        private BucketItr(long offset) {
            this.offset = offset;
            try {
                this.raf = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean hasNext() {
            return offset < end - 16;
        }

        @Override
        public BucketEntry next() {
            int keySize, valueSize;
            long keyIndex, valueIndex, tst = 0;
            byte[] key = new byte[0], val = new byte[0];
            try{
                raf.seek(offset);
                tst = raf.readLong();
                keyIndex = offset + 8;
                valueIndex = keyIndex + 4;
                raf.seek(keyIndex);
                keySize = raf.readInt();
                raf.seek(valueIndex);
                valueSize = raf.readInt();

                key = new byte[keySize];
                val = new byte[valueSize];
                offset += 4;
                raf.seek(offset);
                raf.read(key);
                offset += keySize;
                raf.seek(offset);
                raf.read(val);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return BucketEntry.builder()
                    .setTstamp(tst)
                    .setKey(key)
                    .setValue(val)
                    .build();
        }
    }
}
