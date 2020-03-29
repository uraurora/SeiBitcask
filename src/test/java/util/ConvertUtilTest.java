package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertUtilTest {

    final static int num = 129;
    final static long longNum = 100000L;

    @Test
    public void int2Bytes() {
        byte[] int2bytes = ConvertUtil.int2Bytes(num);
        assertArrayEquals(int2bytes, new byte[]{0, 0, 0, -127});
    }

    @Test
    public void bytes2Int() {
        int bytes2int = ConvertUtil.bytes2Int(new byte[]{0, 0, 0, -127});
        assertEquals(bytes2int, 129);
    }

    @Test
    public void int2OneByte() {
        byte int2byte = ConvertUtil.int2OneByte(num);
        assertEquals(int2byte, -127);
    }

    @Test
    public void oneByte2Int() {
        int in = ConvertUtil.oneByte2Int((byte)-127);
        assertEquals(in, 129);
    }

    @Test
    public void long2Bytes() {
        byte[] int2bytes = ConvertUtil.long2Bytes(longNum);
        assertArrayEquals(int2bytes, new byte[]{0, 0, 0, 0, 0, 1, -122, -96});
    }

    @Test
    public void bytes2Long() {
        long bytes2int = ConvertUtil.bytes2Long(new byte[]{0, 0, 0, 0, 0, 1, -122, -96});
        assertEquals(bytes2int, 100000);
    }

    @Test
    public void t(){
        assertEquals(16 - 1<<3, 120);
        assertEquals(16 - (1<<3), 8);

        for (long i = 0; i < 10000000L; i++) {
            assertEquals(ConvertUtil.bytes2Long(ConvertUtil.long2Bytes(i)), i);
        }

    }
}