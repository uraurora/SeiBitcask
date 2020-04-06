package file.entity;

import file.dto.BucketEntryDto;
import util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class Bucket implements Iterable<BucketEntryDto>{

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
    public Iterator<BucketEntryDto> iterator() {
        return new BucketItr(0L);
    }

    /**
     * bucket文件的迭代器，读取bucket文件的每一个条目，返回一个bucketEntry的迭代器对象
     */
    private class BucketItr implements Iterator<BucketEntryDto>{

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
        public BucketEntryDto next() {
            int keySize, valueSize;
            long tst = 0;
            byte[] key = new byte[0], val = new byte[0];
            try{
                raf.seek(offset);

                tst = raf.readLong();
                keySize = raf.readInt();
                valueSize = raf.readInt();

                key = new byte[keySize]; val = new byte[valueSize];

                raf.read(key); raf.read(val);

                offset += 16 + valueSize + keySize;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return BucketEntryDto.builder()
                    .setTstamp(tst)
                    .setKey(key)
                    .setValue(val)
                    .setOffset(offset)
                    .build();
        }

        public void close(){
            try {
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
