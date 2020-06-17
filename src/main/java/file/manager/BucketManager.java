package file.manager;

import com.sun.istack.internal.NotNull;
import core.constant.FileConstEnum;
import config.GlobalConstant;
import file.cache.BucketBuffer;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/***
 * @author sei
 * @description Bucket文件的管理类
 * @date 18:45 2020/3/28
 */
public class BucketManager implements IBucketManager {
    /**
     * save, remove, update, get
     * insert, delete, update, select
     * 实现键值的增删改查，其中增改是一样的实现，删需要处理一下BucketEntry，查一是文件的查，二是缓存查
     */
    private final BucketBuffer buffer;

    private BucketManager(BucketBuffer buffer){
        this.buffer = buffer;
    }

    public static BucketManager newInstance(BucketBuffer buffer){
        return new BucketManager(buffer);
    }

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock writeLock = lock.writeLock();

    private final Lock readLock = lock.readLock();

    //<editor-fold desc="Operations">
    /**
     * 将bucketEntry序列化好的对象写入Bucket文件
     * @param entry 序列化好的对象
     */
    @Override
    public void writeBucket(@NotNull BucketEntry entry){
        Path target;
        final long offset;
        writeLock.lock();
        try {
            target = FileUtil.getPath(buffer.getActiveBucketId());

            offset = Files.size(target);
            if(offset > GlobalConstant.BUCKET_MAX_SIZE - entry.size()){
                // 新建bucket文件
                target = FileUtil.getPath(buffer.idIncrementAndGet());
            }
            Bucket.newInstance(target).write(entry);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 读取bucketId,pos,offset，读入指定位置的序列化对象
     * @param indexEntry 索引文件条目，内部包含要找的对象的位置信息
     * @return 所查找的依据某种序列化的对象
     */
    @Override
    public byte[] readBucket(@NotNull IndexEntry indexEntry){
        Path target;
        byte[] res = new byte[0];
        // TODO:cache
        // buffer read
        // if(indexEntry.getBucketId() == )
        readLock.lock();
        try {
            target = FileUtil.getPath(indexEntry.getBucketId(), FileConstEnum.BUCKET_PREFIX);
            res = Bucket.newInstance(target).read(indexEntry);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return res;
    }
    //</editor-fold>

    @Override
    public long bucketSize(int id){
        long res = 0;
        readLock.lock();
        try{
            res = Files.size(FileUtil.getPath(id));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return res;
    }

    @Override
    public int getActiveBucketId(){
        return buffer.getActiveBucketId();
    }

    @Override
    public void release(){
        buffer.closeSimpleBuffer();
        buffer.closeBucketBuffer();
    }

}
