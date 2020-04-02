package file.cache;

import org.junit.Test;
import util.ConvertUtil;
import util.FileUtil;

import java.io.File;

import static org.junit.Assert.*;

public class BucketBufferTest {

    BucketBuffer buffer = BucketBuffer.newInstance();

    @Test
    public void getActiveBucketId() {
//        buffer.setActiveBucketId(12);
//        assertEquals(buffer.getActiveBucketId(), 12);
//        buffer.setActiveBucketId(15);
//        assertEquals(buffer.getActiveBucketId(), 15);
        for (int i = 0; i < 100; i++) {
            buffer.setActiveBucketId(i);
            assertEquals(buffer.getActiveBucketId(), i);
        }
        buffer.closeSimpleBuffer();
    }

    @Test
    public void setActiveBucketId() {
        System.out.println(buffer.getActiveBucketId());
        System.out.println(new File(".//file//cache.txt").length());
        buffer.closeSimpleBuffer();
    }

}