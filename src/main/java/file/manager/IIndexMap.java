package file.manager;

import file.entity.IndexEntry;

public interface IIndexMap<K, V> {
    boolean isExisted(K key);

    void put(K key, V val);

    void remove(K key);

    IndexEntry get(K key);
}
