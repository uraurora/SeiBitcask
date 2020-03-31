package cache;

import core.constant.FileCategory;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;

public class SimpleBuffer<T> {

    private final MappedByteBuffer buffer;

    private SimpleBuffer(File file, String mode) throws IOException {
        buffer = BufferPool.getInstance().newBuffer(file, mode, 8);
    }

    public static <T> SimpleBuffer<T> getInstance(File file, String mode) throws IOException {
        return new SimpleBuffer<>(file, mode);
    }

}
