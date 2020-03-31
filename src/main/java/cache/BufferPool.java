package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BufferPool {

    private BufferPool(){}

    private static class BufferPoolHolder{
        private final static BufferPool instance = new BufferPool();
    }

    public static BufferPool getInstance(){
        return BufferPoolHolder.instance;
    }


    public MappedByteBuffer newBuffer(File file, String mode, int size) throws IOException {
        return new RandomAccessFile(file, mode)
                .getChannel()
                .map(FileChannel.MapMode.READ_WRITE, 0, size);
    }

}
