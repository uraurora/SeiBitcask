package core.db;

public interface IBitcask {

    boolean put(String key, Object value);

    boolean remove(String key);

    Object get(String key);
}
