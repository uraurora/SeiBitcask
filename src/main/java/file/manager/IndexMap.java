package file.manager;

import core.db.BitcaskBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author sei
 * @description 内存中的文件索引管理类(因为管理了单机所有Bitcask实例的索引，务必简洁实现)
 * @date 17:26 2020/3/28
 */
public class IndexMap<K, V> {
    // TODO: 避免原型使用
    private final ConcurrentMap<K, V> map;

    //<editor-fold desc="单例模式">
    private IndexMap(){this.map = new ConcurrentHashMap<>();}

    private static class IndexMapHolder{
        private static final IndexMap instance = new IndexMap();
    }

    public static IndexMap getInstance(){
        return IndexMapHolder.instance;
    }
    //</editor-fold>

    public boolean isExisted(K key){
        return map.containsKey(key);
    }

    public void put(K key, V val){
        map.put(key, val);
    }

    public void remove(K key){
        map.remove(key);
    }

}
