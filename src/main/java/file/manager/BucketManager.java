package file.manager;

import core.constant.FileConstEnum;
import core.constant.StaticVar;
import file.cache.BucketBuffer;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import util.ConvertUtil;
import util.FileUtil;

import java.io.File;
import java.util.ArrayList;
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
public class BucketManager {
    /**
     * save, remove, update, get
     * insert, delete, update, select
     * 实现键值的增删改查，其中增改是一样的实现，删需要处理一下BucketEntry，查一是文件的查，二是缓存查
     */
    private BucketBuffer buffer;

    private BucketManager(){
        this.buffer = BucketBuffer.getInstance();
    }

    public static BucketManager newInstance(){
        return new BucketManager();
    }

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock writeLock = lock.writeLock();

    private final Lock readLock = lock.readLock();

    //<editor-fold desc="Operations">
    /**
     * 将bucketEntry序列化好的对象写入Bucket文件
     * @param entry 序列化好的对象
     */
    public void writeBucket(BucketEntry entry){
        File target;
        LongAdder offset = new LongAdder();
        AtomicInteger id = new AtomicInteger(buffer.getActiveBucketId());

        target = FileUtil.getFile(id.get(), FileConstEnum.BUCKET_PREFIX);
        offset.add(target.length());
        if(offset.longValue() > StaticVar.BUCKET_MAX_SIZE - entry.size()){
            // 新建bucket文件
            target = FileUtil.getFile(id.incrementAndGet(), FileConstEnum.BUCKET_PREFIX);
            offset.reset();
            buffer.setActiveBucketId(id.get());
        }

        writeLock.lock();
        try {
            FileUtil.write(target, entry.toBytes(), offset.longValue());
        }
        finally {
            writeLock.unlock();
        }

        offset.add(entry.size());
        buffer.setBucketOffset(offset.longValue());
    }

    /**
     * 读取bucketId,pos,offset，读入指定位置的序列化对象
     * @param indexEntry 索引文件条目，内部包含要找的对象的位置信息
     * @return 所查找的依据某种序列化的对象
     */
    public byte[] readBucket(IndexEntry indexEntry){
        File target;
        byte[] res;
        // TODO:cache
        // buffer read
        // if(indexEntry.getBucketId() == )
        readLock.lock();
        try {
            target = FileUtil.getFile(indexEntry.getBucketId(), FileConstEnum.BUCKET_PREFIX);
            res = FileUtil.read(target, indexEntry.getOffset(), indexEntry.getValueSize());
        } finally {
            readLock.unlock();
        }
        return res;
    }

    public void setActiveBucketId(int id){

    }
    //</editor-fold>

}
