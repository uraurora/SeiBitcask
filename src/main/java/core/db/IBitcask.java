package core.db;

import java.util.Optional;

public interface IBitcask {

    boolean put(String key, String value);

    boolean remove(String key);

    Optional<String> get(String key);
}
