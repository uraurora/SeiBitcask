package file.cache;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

public class BufferPool {

    private BufferPool(){}

    private static class BufferPoolHolder{
        private final static BufferPool instance = new BufferPool();
    }

    public static BufferPool getInstance(){
        return BufferPoolHolder.instance;
    }


    public static Optional<MappedByteBuffer> newBuffer(File file, String mode, int size) {
        Optional<MappedByteBuffer> res = Optional.empty();
        try{
            res = Optional.of(new RandomAccessFile(file, mode)
                    .getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, size)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Optional<MappedByteBuffer> newSimpleBuffer(File file, String mode) {
        return newBuffer(file, mode, 8);
    }

}
