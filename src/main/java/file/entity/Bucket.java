package file.entity;

import file.dto.BucketEntryDto;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        final List<BucketEntryDto> list = new ArrayList<>();
        final long end = file.length();
        int keySize, valueSize;
        long tst, offset = 0;
        byte[] key, val;
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")){
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
        return list.iterator();
    }


}
