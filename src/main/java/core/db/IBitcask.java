package core.db;

import java.util.Optional;

public interface IBitcask {

    boolean put(String key, Object value);

    boolean remove(String key);

    Optional<Object> get(String key);

    <T> T get(String key, Class<T> clazz);
}
