package file.manager;

import file.entity.BucketEntry;
import file.entity.IndexEntry;
import util.ConvertUtil;

import java.util.ArrayList;

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

    private BucketManager(){}

    public static BucketManager newInstance(){
        return new BucketManager();
    }

    //<editor-fold desc="Operations">
    /**
     * 将bucketEntry序列化好的对象写入Bucket文件
     * @param entry 序列化好的对象
     */
    public void writeBucket(BucketEntry entry){
    }

    /**
     * 读取bucketId,pos,offset，读入指定位置的序列化对象
     * @param indexEntry 索引文件条目，内部包含要找的对象的位置信息
     * @return 所查找的依据某种序列化的对象
     */
    public BucketEntry readBucket(IndexEntry indexEntry){
        return null;
    }

    public void setActiveBucketId(int id){

    }
    //</editor-fold>




    //<editor-fold desc="Static Query Methods">
    public static int getActiveBucketId(){
        return 0;
    }

    /**
     * 获取指定bucketId的容量，byte
     * @param bucketId bucket文件的id
     * @return 容量大小，单位byte
     */
    public static long getSize(int bucketId){
        return 0L;
    }
    //</editor-fold>


}
