package file.entity;

import file.dto.BucketEntryDto;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Bucket implements Iterable<BucketEntryDto>{

    private final Path path;

    private Bucket(Path path) {
        this.path = path;
    }

    public static Bucket newInstance(Path path) {
        return new Bucket(path);
    }

    public void write(BucketEntry entry) {
        FileUtil.writeTail(path, entry.toBytes());
    }

    public byte[] read(IndexEntry indexEntry) {
        return FileUtil.read(path.toFile(), indexEntry.getOffset() - indexEntry.getValueSize(), indexEntry.getValueSize());
    }

    @Override
    public Iterator<BucketEntryDto> iterator() {
        Iterator<BucketEntryDto> res = null;
        try {
            res = asList().iterator();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
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


}
