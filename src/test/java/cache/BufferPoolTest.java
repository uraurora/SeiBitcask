package cache;

import org.junit.Test;
import util.ConvertUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import static org.junit.Assert.*;

public class BufferPoolTest {



    @Test
    public void getActiveBucketId() throws IOException {
        final RandomAccessFile raf = new RandomAccessFile("./test.txt", "rw");
        byte[] a = ConvertUtil.int2Bytes(129);
        for (int i = 0; i < 5; i++) {
            raf.write(a);
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(raf.read());
        }
    }
}