package file.manager;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.sun.istack.internal.NotNull;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import core.constant.FileConstEnum;
import config.GlobalConstant;
import file.cache.BucketBuffer;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import util.FileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private final Logger logger = LoggerFactory.getLogger(BucketManager.class);

    private final String bitcaskId;

    private final BucketBuffer buffer;

    private BucketManager(String bitcaskId, BucketBuffer buffer) {
        this.bitcaskId = bitcaskId;
        this.buffer = buffer;
    }

    public static BucketManager newInstance(String id, BucketBuffer buffer){
        return new BucketManager(id, buffer);
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
            logger.error("bitcaskId : " + bitcaskId + ", write error ");
        } finally {
            writeLock.unlock();
            logger.trace("bitcaskId : " + bitcaskId + ", write successful ");
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
            logger.error("bitcaskId : " + bitcaskId + ", read error ");
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
            logger.error("cannot create Bucket or get instance");
        }
        return res;
    }

    @Override
    public boolean newBucket() {
        getBucket(buffer.idIncrementAndGet());
        return true;
    }

    @Override
    public boolean removeBucket(int id) {
        try {
            return removeBucket(FileUtil.getPath(id));
        } catch (IOException e) {
            logger.warn("bitcaskId : " + bitcaskId + ", remove bucket failed : " + id);
            return false;
        }
    }

    @Override
    public boolean removeBucket(Path path) {
        try {
            Files.deleteIfExists(path);
            return true;
        } catch (IOException e) {
            logger.warn("bitcaskId : " + bitcaskId + ", remove bucket failed : " + path);
            return false;
        }
    }
    //</editor-fold>

    /**
     * 找到这个bitcask实例下所有的buckets
     * @return
     */
    public List<Bucket> getBuckets(){
        List<Bucket> res = Lists.newArrayList();
        // TODO:文件夹位置
        // 先得到该bucketManager管理的文件夹path，之后遍历文件返回
        return res;
    }

    @Override
    public long bucketSize(int id){
        long res = 0;
        readLock.lock();
        try{
            res = Files.size(FileUtil.getPath(id));
        } catch (IOException e) {
            logger.warn("bitcaskId : " + bitcaskId + ", cannot get size of bucket " + id);
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
