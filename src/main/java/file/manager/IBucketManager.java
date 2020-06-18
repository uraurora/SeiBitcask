package file.manager;

import com.sun.istack.internal.NotNull;
import file.entity.Bucket;
import file.entity.BucketEntry;
import file.entity.IndexEntry;

import java.nio.file.Path;

public interface IBucketManager {

    void write(@NotNull BucketEntry entry);

    byte[] read(@NotNull IndexEntry indexEntry);

    Bucket getBucket(int id);

    long bucketSize(int id);

    int getActiveBucketId();

    void release();
}
