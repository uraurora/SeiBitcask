package cache;

import core.constant.FileCategory;
import org.junit.Test;
import util.ConvertUtil;
import util.FileUtil;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BufferPoolTest {



    @Test
    public void getActiveBucketId() throws IOException {
          final RandomAccessFile raf = new RandomAccessFile("./test.txt", "rw");
        byte[] a = new byte[]{'h', 'e', 'l', 'l', 'o'};
        for (int i = 0; i < 5; i++) {
            raf.write(a);
        }

        byte[] res = new byte[5];
        for (int i = 0; i < 5; i++) {
            System.out.println(raf.read(res));
            System.out.println(Arrays.toString(res));
        }
    }

    @Test
    public void fileTest() throws IOException {
        File file = new File(".//test.txt");
        Byte read = FileUtil.read(file, 5, FileCategory.BYTE);
        System.out.println(read);

        System.out.println(Arrays.toString(FileUtil.read(file, 5, 5)));
    }

    @Test
    public void append(){
        File file = new File(".//test.txt");
        byte[] a = new byte[]{'s', 'e', 'i'};
        FileUtil.writeTail(file, a);
    }

    @Test
    public void append2(){
        File file = new File(".//test.txt");
        byte[] a = new byte[]{'s', 'e', 'i'};
        FileUtil.write(file, a, file.length());
    }
}