package file.manager;

import file.cache.BucketBuffer;
import file.dto.BucketEntryDto;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import org.junit.Test;
import util.FileUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

public class BucketManagerTest {

    BucketManager bucketManager = BucketManager.newInstance(BucketBuffer.newInstance());

    @Test
    public void writeBucket() {
        BucketEntry e = BucketEntry.builder().setTstamp(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")))
                .setKey("key2".getBytes())
                .setValue("how can things be so terrible?".getBytes())
                .build();
        System.out.println("key2".getBytes().length);
        System.out.println("how can things be so terrible?".getBytes().length);
        bucketManager.writeBucket(e);
    }

    @Test
    public void readBucket() {
        // System.out.println(FileUtil.getPath(0));
        IndexEntry e = IndexEntry.builder()
                .setTstamp(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")))
                .setBucketId(0)
                .setValueSize(30)
                .setOffset(198)
                .build();
        byte[] bytes = bucketManager.readBucket(e);
        System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void bucketTest() throws IOException {
        Bucket b = Bucket.newInstance(FileUtil.getPath(0));
        for (BucketEntryDto e : b) {
            System.out.println(e);
        }
    }

}