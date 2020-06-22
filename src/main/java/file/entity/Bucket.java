package file.entity;

import file.dto.BucketEntryDto;
import util.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bucket implements Iterable<BucketEntry>{

    private final Path path;

    private long size;

    private Bucket(Path path) throws IOException {
        if (Files.exists(path)) {
            this.path = path;
            this.size = Files.size(path);
        } else{
            this.path = null;
            this.size = 0;
        }
    }

    public static Bucket newInstance(Path path) throws IOException {
        return new Bucket(path);
    }

    public void write(BucketEntry entry) throws IOException {
        Files.write(path, entry.toBytes(), StandardOpenOption.APPEND);
        size += entry.size();
    }

    public byte[] read(IndexEntry indexEntry) throws IOException {
        return FileUtil.read(path.toFile(), indexEntry.getOffset() - indexEntry.getValueSize(), indexEntry.getValueSize());
    }

    public Path getPath(){
        return path;
    }

    public long size(){
        return size;
    }

    @Override
    public Iterator<BucketEntry> iterator() {
        try {
            return new BucketIter(0, new RandomAccessFile(path.toFile(), "r"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BucketEntryDto> asList() throws IOException {
        final List<BucketEntryDto> list = new ArrayList<>();
        final long end = Files.size(path);
        int keySize, valueSize;
        long tst, offset = 0;
        byte[] key, val;
        try(RandomAccessFile raf = new RandomAccessFile(path.toFile(), "r")){
            while(offset < end - 16){
                raf.seek(offset);

                tst = raf.readLong();
                keySize = raf.readInt();
                valueSize = raf.readInt();

                key = new byte[keySize]; val = new byte[valueSize];

                raf.read(key); raf.read(val);

                offset += 16 + valueSize + keySize;

                BucketEntryDto dto = BucketEntryDto.builder()
                        .setTstamp(tst)
                        .setKey(key)
                        .setValue(val)
                        .setOffset(offset)
                        .build();
                list.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private class BucketIter implements Iterator<BucketEntry>{

        private int offset;

        private final RandomAccessFile raf;

        private BucketIter(int offset, RandomAccessFile raf) {
            this.offset = offset;
            this.raf = raf;
        }


        @Override
        public boolean hasNext() {
            return offset < size() - 16;
        }

        @Override
        public BucketEntry next() {
            int keySize, valueSize;
            long tst;
            byte[] key, val;
            try {
                raf.seek(offset);

                tst = raf.readLong();
                keySize = raf.readInt();
                valueSize = raf.readInt();

                key = new byte[keySize];
                val = new byte[valueSize];

                raf.read(key);
                raf.read(val);

                offset += 16 + valueSize + keySize;

                BucketEntry entry = BucketEntry.builder()
                        .setTstamp(tst)
                        .setKey(key)
                        .setValue(val)
                        .build();
                return entry;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "Bucket{" +
                "path=" + path +
                '}';
    }


}
