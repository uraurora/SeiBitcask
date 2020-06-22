package file.manager;

import com.sun.istack.internal.NotNull;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;

import java.nio.file.Path;
import java.util.List;

public interface IBucketManager {

    /**
     * 写入bucketEntry
     * @param entry value object
     */
    void write(@NotNull BucketEntry entry);

    /**
     * 根据indexEntry读取bucket的信息
     * @param indexEntry value object
     * @return 根据indexEntry读取bucket的信息
     */
    byte[] read(@NotNull IndexEntry indexEntry);

    /**
     * 获取Bucket对象，如果不存在则新建
     * @param id bucket文件id
     * @return bucket实例
     */
    Bucket getBucket(int id);

    /**
     * 新建Bucket
     * @return 是否成功
     */
    boolean newBucket();

    /**
     * 删除bucket文件，成功返回true
     * @param id bucket文件id
     * @return 成功与否
     */
    boolean removeBucket(int id);
    /**
     * 删除bucket文件，成功返回true
     * @param path bucket文件path
     * @return 成功与否
     */
    boolean removeBucket(Path path);

    /**
     * 获取该bitcask下管理的buckets
     * @return bucket列表
     */
    List<Bucket> getBuckets();

    long bucketSize(int id);

    int getActiveBucketId();

    void release();
}
