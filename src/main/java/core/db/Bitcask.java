package core.db;

import cache.BufferPool;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import file.manager.BucketManager;
import file.manager.IndexMap;
import util.SerializeUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * @author sei
 * @description Bitcask
 * @date 17:12 2020/3/28
 */
public class Bitcask implements IBitcask {

    private final BucketManager bucketManager;

    private final IndexMap indexMap;

    public Bitcask(BucketManager bucketManager, IndexMap indexMap) {
        this.bucketManager = bucketManager;
        this.indexMap = indexMap;
    }

    /**
     * 1、序列化key和value，并生成bucketEntry
     * 2、调用BucketManager的write方法写入缓存区
     * 3、更新IndexMap的对应的键值
     * @param key 键
     * @param value 值
     * @return 是否成功
     */
    @Override
    public boolean put(String key, Object value) {
        // serialize the key and val, generate bucketEntry
        long tstamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        BucketEntry bucketEntry = BucketEntry.builder()
                .setTstamp(tstamp)
                .setKey(SerializeUtil.serialize(Objects.requireNonNull(key)))
                .setValue(SerializeUtil.serialize(Objects.requireNonNull(value)))
                .build();

        // write the entry to buffer/file
        bucketManager.writeBucket(bucketEntry);

        // update the indexMap
        IndexEntry indexEntry = IndexEntry.builder()
                .setTstamp(tstamp)
                .setBucketId(BufferPool.getActiveBucketId())
                //.setOffset()        // TODO: 获取该条目在文件的偏移量
                .setValueSize(bucketEntry.getValueSize())
                .build();
        indexMap.put(key, indexEntry);
        return true;
    }

    /**
     * 1、序列化key和value，并生成bucketEntry，逻辑删除标识
     * 2、调用BucketManager的write方法写入缓存区
     * 3、删除IndexMap的键
     * @param key 需要删除的键值对的键
     * @return 是否成功
     */
    @Override
    public boolean remove(String key) {
        return true;
    }

    /**
     * 1、在IndexMap中查找键是否存在，不存在返回信息
     * 2、存在的话得到IndexEntry，判断bucketId为active bucketId，是的话从缓存区中查找，
     * 找不到或不是active则调用BucketManager的read方法获取bucketEntry
     * 3、反序列化值对象得到结果返回
     * @param key 查询的键
     * @return 查询结果
     */
    @Override
    public Object get(String key) {

        return null;
    }
}
