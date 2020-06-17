package util;

import core.constant.FileCategory;
import core.constant.FileConstEnum;
import config.GlobalConstant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
    public static byte[] read(File file, long offset, int size){
        byte[] res = new byte[size];
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            raf.read(res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void write(File file, byte[] bytes, long offset){
        try(RandomAccessFile raf = new RandomAccessFile(file, "rwd")) {
            raf.seek(offset);
            raf.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T read(File file, long offset, FileCategory category){
        T res = null;
        try(RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(offset);
            res = (T) category.read(raf);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static <T> boolean write(File file, T obj, long offset, FileCategory category){
        try(RandomAccessFile raf = new RandomAccessFile(file, "rws")) {
            raf.seek(offset);
            category.write(raf, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void writeTail(Path file, byte[] bytes){

        try(OutputStream os = Files.newOutputStream(file, StandardOpenOption.APPEND)){
            os.write(bytes);
            os.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    //</editor-fold>

    public static Path getPath(int id, FileConstEnum type){
        return Paths.get(fileName(id, type));
    }

    public static Path getPath(int id){
        return Paths.get(fileName(id, FileConstEnum.BUCKET_PREFIX));
    }

    public static Path getSimpleBufferPath(){
        return Paths.get(GlobalConstant.FILE_DIR+ GlobalConstant.CACHE_FILE_NAME);
    }

    private static String fileName(int id, FileConstEnum type){
        return "./" + type.getVal() + id + FileConstEnum.DATA_SUFFIX.getVal();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }
}
