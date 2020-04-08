package task.merge;

public interface IMergeStrategy {

    void newBuckets();

    void removeBuckets();

    void newHint();

    void updateIndex();

    default void merge(){
        newBuckets();
        newHint();
        updateIndex();
        removeBuckets();
    }

}
