package file.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
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

    private BucketManager(BucketBuffer buffer) {
        this.buffer = buffer;
    }

    public static BucketManager newInstance(){
        return new BucketManager(BucketBuffer.newInstance());
    }

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock writeLock = lock.writeLock();

    private final Lock readLock = lock.readLock();

    private final LoadingCache<Path, Bucket> bucketCache = CacheBuilder.newBuilder()
            .maximumSize(GlobalConstant.BUCKET_MAX_NUM)
            .build(new CacheLoader<Path, Bucket>() {
                @Override
                public Bucket load(Path path) throws Exception {
                    return Bucket.newInstance(path);
                }
            });

    //<editor-fold desc="Operations">
    /**
     * 将bucketEntry序列化好的对象写入Bucket文件
     * @param entry 序列化好的对象
     */
    @Override
    public void write(@NotNull BucketEntry entry){
        Path path;
        writeLock.lock();
        try {
            path = FileUtil.getPath(buffer.getActiveBucketId());
            if(Files.size(path) > GlobalConstant.BUCKET_MAX_SIZE - entry.size()){
                // 新建bucket文件路径
                path = FileUtil.getPath(buffer.idIncrementAndGet());
            }
            // 获取旧bucket或者新建bucket写文件
            bucketCache.get(path).write(entry);
        } catch (IOException | ExecutionException e) {
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
    public byte[] read(@NotNull IndexEntry indexEntry){
        Path path;
        byte[] res = new byte[0];
        readLock.lock();
        try {
            // TODO:cache
            // buffer read
            // if(indexEntry.getBucketId() == )
            path = FileUtil.getPath(indexEntry.getBucketId(), FileConstEnum.BUCKET_PREFIX);
            res = bucketCache.get(path).read(indexEntry);
        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
        return res;
    }

    @Override
    public Bucket getBucket(int id) {
        Bucket res = null;
        try {
            res = bucketCache.get(FileUtil.getPath(id));
        } catch (ExecutionException | IOException e) {
            e.printStackTrace();
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
