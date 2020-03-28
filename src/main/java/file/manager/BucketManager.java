package file.manager;

/***
 * @author sei
 * @description Bucket文件的管理类
 * @date 18:45 2020/3/28
 */
public class BucketManager {
    /**
     * save, remove, update, get
     * insert, delete, update, select
     * 实现键值的增删改查，其中增改是一样的实现，删需要处理一下BucketEntry，查一是文件的查，二是缓存查
     */

    private BucketManager(){}

    public static BucketManager getInstance(){
        return new BucketManager();
    }

    public void insert(){
        /*
         * 首先写入缓存，之后缓存sync写入文件
         */
    }

    public void delete(){
        /*
         *
         */
    }

    public boolean update(){
        try{
            insert();
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
}
