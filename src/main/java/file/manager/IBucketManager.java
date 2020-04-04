package file.manager;

import file.entity.BucketEntry;
import file.entity.IndexEntry;

public interface IBucketManager {
    void writeBucket(BucketEntry entry);

    byte[] readBucket(IndexEntry indexEntry);

    long bucketSize(int id);

    int getActiveBucketId();

    void release();
}
