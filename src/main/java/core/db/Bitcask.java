package core.db;

import core.constant.EntrySizeEnum;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import file.manager.IBucketManager;
import file.manager.IIndexMap;
import core.constant.BucketSerializeCategory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/**
 * @author sei
 * @description Bitcask
 * @date 17:12 2020/3/28
 */
public class Bitcask implements IBitcask {

    private final IBucketManager bucketManager;

    private final IIndexMap<String, IndexEntry> indexMap;

    private final BucketSerializeCategory category;

    public Bitcask(IBucketManager bucketManager, IIndexMap<String, IndexEntry> indexMap, BucketSerializeCategory category) {
        this.bucketManager = bucketManager;
        this.indexMap = indexMap;
        this.category = category;
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
                .setKey(check(category.serialize(Objects.requireNonNull(key)), EntrySizeEnum.KEY_MAX_SIZE.getVal()))
                .setValue(check(category.serialize(value), EntrySizeEnum.VAL_MAX_SIZE.getVal()))
                .build();

        // write the entry to buffer/file
        bucketManager.writeBucket(bucketEntry);

        // update the indexMap
        int id = bucketManager.getActiveBucketId();
        IndexEntry indexEntry = IndexEntry.builder()
                .setTstamp(tstamp)
                .setBucketId(id)
                .setOffset(bucketManager.bucketSize(id))
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
        return put(key, null);
    }


    @Override
    public Optional<Object> get(String key) {
        return Optional.ofNullable(getObject(key));
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(getObject(key));
    }

    /**
     * 1、在IndexMap中查找键是否存在，不存在返回信息
     * 2、存在的话得到IndexEntry，判断bucketId为active bucketId，是的话从缓存区中查找，
     * 找不到或不是active则调用BucketManager的read方法获取bucketEntry
     * 3、反序列化值对象得到结果返回
     * @param key 查询的键
     * @return 查询结果
     */
    private Object getObject(String key){
        Object res = null;
        if(indexMap.isExisted(key)){
            IndexEntry indexEntry = indexMap.get(key);
            // read from buffer, aim to find the val
            res = category.deserialize(bucketManager.readBucket(indexEntry));
        }
        return res;
    }

    private static byte[] check(byte[] obj, int size){
        if(obj == null || obj.length > size){
            throw new IllegalArgumentException(String.format("the bytes size is bound with: %d", size));
        }
        return obj;
    }

}
