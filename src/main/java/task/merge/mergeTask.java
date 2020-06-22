package task.merge;


import com.google.common.collect.Maps;
import core.constant.BucketSerializeCategory;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;
import file.manager.IBucketManager;
import file.manager.IIndexMap;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public final class mergeTask extends AbstractMergeTemplate implements IMergeTemplate{

    protected final IBucketManager bucketManager;

    protected final IIndexMap<String, IndexEntry> indexMap;

    private final ConcurrentMap<String, BucketEntry> map = Maps.newConcurrentMap();

    private final BucketSerializeCategory category;

    protected mergeTask(IBucketManager bucketManager, IIndexMap<String, IndexEntry> indexMap,  BucketSerializeCategory category) {
        this.bucketManager = bucketManager;
        this.indexMap = indexMap;
        this.category = category;
    }

    /**
     * 新建创建好的bucket，将merge好的entry写入bucket(同步，异步)
     * 必须新建，如果使用旧bucket，可能会在结束时删除，所以必须新建
     */
    @Override
    public void newBuckets() {
        bucketManager.newBucket();
    }

    /**
     * 删除merge之前的buckets
     */
    @Override
    public void removeBuckets() {

    }

    @Override
    public void newHint() {

    }

    /**
     * 由于新的bucket建立，key和value的位置都变化了，于是需要更新indexMap
     */
    @Override
    public void updateIndex() {

    }

    public void merge(){
        List<Bucket> buckets = bucketManager.getBuckets();
        buckets.forEach(bucket->
            new Thread(()->mergeBucket(bucket))
        );

        // 新建buckets
        newBuckets();

        map.forEach((key, value) -> {
            // 将整理好的entry写入bucket
            long tstamp = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
            bucketManager.write(value);

            int id = bucketManager.getActiveBucketId();
            long offset = bucketManager.getBucket(id).size();

            // 更新indexMap
            IndexEntry index = IndexEntry.builder()
                    .setBucketId(id)
                    .setOffset(offset)
                    .setTstamp(tstamp)
                    .setValueSize(value.getValueSize())
                    .build();
            indexMap.put(key, index);
        });


        // 异步删除旧的buckets文件
        buckets.forEach(b->bucketManager.removeBucket(b.getPath()));
        // 新建hint文件


    }

    private void mergeBucket(Bucket bucket){
        // 将一个bucket的entry按时间最新存入map
        for(BucketEntry entry : bucket){
            String key = category.deserialize(entry.getKey());
            if(map.containsKey(key)){
                BucketEntry e = map.get(key);
                if(entry.compareTo(e) > 0){
                    map.put(key, entry);
                }
            }else{
                map.put(key, entry);
            }
        }

    }

}
