package file.manager;

import core.db.BitcaskBuilder;
import file.entity.IndexEntry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author sei
 * @description 内存中的文件索引管理类(因为管理了单机所有Bitcask实例的索引，务必简洁实现)
 * @date 17:26 2020/3/28
 */
public class IndexMap {

    private final ConcurrentMap<String, IndexEntry> map;

    //<editor-fold desc="单例模式">
    private IndexMap(){this.map = new ConcurrentHashMap<>();}

    private static class IndexMapHolder{
        private static final IndexMap instance = new IndexMap();
    }

    public static IndexMap getInstance(){
        return IndexMapHolder.instance;
    }
    //</editor-fold>

    public boolean isExisted(String key){
        return map.containsKey(key);
    }

    public void put(String key, IndexEntry val){
        map.put(key, val);
    }

    public void remove(String key){
        map.remove(key);
    }

    public IndexEntry get(String key){
        return map.get(key);
    }

}
