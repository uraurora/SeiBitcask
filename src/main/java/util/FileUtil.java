package util;

import core.constant.FileCategory;
import core.constant.FileConstEnum;
import config.GlobalConstant;

import java.io.*;
import java.nio.file.*;

public final class FileUtil {
    //TODO: logger
    /**
     * 文件操作工具类，负责数据库持久化文件的读写
     *
     * 指定文件的指定位置字节数组读写
     * 指定文件末尾字节数组追加
     * 指定文件容量大小
     */

    private FileUtil(){}

    //<editor-fold desc="file operations">
    public static byte[] read(File file, long offset, int size) throws IOException {
        byte[] res = new byte[size];
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            raf.read(res);
            return res;
        }
    }

    public static void write(File file, byte[] bytes, long offset) throws IOException {
        try(RandomAccessFile raf = new RandomAccessFile(file, "rwd")) {
            raf.seek(offset);
            raf.write(bytes);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(File file, long offset, FileCategory category) throws IOException {
        T res = null;
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            res = (T) category.read(raf);
            return res;
        }
    }

    public static <T> boolean write(File file, T obj, long offset, FileCategory category) throws IOException {
        try(RandomAccessFile raf = new RandomAccessFile(file, "rws")) {
            raf.seek(offset);
            category.write(raf, obj);
        }
        return true;
    }

    public static void writeTail(Path file, byte[] bytes) throws IOException {
        try(OutputStream os = Files.newOutputStream(file, StandardOpenOption.APPEND)) {
            os.write(bytes);
            os.flush();
        }
    }
    //</editor-fold>

    public static Path getPath(int id, FileConstEnum type){
        return FileSystems.getDefault().getPath(fileName(id, type));
    }

    public static Path getPath(int id) throws IOException {
        Path res = FileSystems.getDefault().getPath(fileName(id, FileConstEnum.BUCKET_PREFIX));
        if (!Files.exists(res)) {
            Files.createFile(res);
        }
        return res;
    }

    public static Path getSimpleBufferPath(){
        return FileSystems.getDefault().getPath(GlobalConstant.FILE_DIR+ GlobalConstant.CACHE_FILE_NAME);
    }

    private static String fileName(int id, FileConstEnum type){
        return "./" + type.getVal() + id + FileConstEnum.DATA_SUFFIX.getVal();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
