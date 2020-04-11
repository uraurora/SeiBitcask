package file.manager;

import com.sun.istack.internal.NotNull;
import file.entity.BucketEntry;
import file.entity.IndexEntry;

public interface IBucketManager {

    void writeBucket(@NotNull BucketEntry entry);

    byte[] readBucket(@NotNull IndexEntry indexEntry);

    long bucketSize(int id);

    int getActiveBucketId();

    void release();
}
