package file.manager;

import com.sun.istack.internal.NotNull;
import core.constant.FileConstEnum;
import core.constant.StaticVar;
import file.cache.BucketBuffer;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import util.FileUtil;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/***
 * @author sei
 * @description Bucket文件的管理类 //TODO:finish the methods
 * @date 18:45 2020/3/28
 */
public class BucketManager implements IBucketManager {
    /**
     * save, remove, update, get
     * insert, delete, update, select
     * 实现键值的增删改查，其中增改是一样的实现，删需要处理一下BucketEntry，查一是文件的查，二是缓存查
     */
    private BucketBuffer buffer;

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
        File target;
        final long offset;
        writeLock.lock();
        try {
            target = FileUtil.getFile(buffer.getActiveBucketId());
            offset = target.length();
            if(offset > StaticVar.BUCKET_MAX_SIZE - entry.size()){
                // 新建bucket文件
                target = FileUtil.getFile(buffer.idIncrementAndGet());
            }
            Bucket.newInstance(target).write(entry);
        }
        finally {
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
        File target;
        byte[] res;
        // TODO:cache
        // buffer read
        // if(indexEntry.getBucketId() == )
        readLock.lock();
        try {
            target = FileUtil.getFile(indexEntry.getBucketId(), FileConstEnum.BUCKET_PREFIX);
            res = Bucket.newInstance(target).read(indexEntry);
        } finally {
            readLock.unlock();
        }
        return res;
    }
    //</editor-fold>

    @Override
    public long bucketSize(int id){
        long res;
        readLock.lock();
        try{
            res = FileUtil.getFile(id).length();
        }
        finally {
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
