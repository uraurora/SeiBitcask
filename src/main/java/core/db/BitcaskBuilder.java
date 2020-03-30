package core.db;

import file.manager.BucketManager;
import file.manager.IndexMap;
import file.serialize.BucketSerializeCategory;

/**
 * @author sei
 * @description Bitcask的实例建造者，单例
 * @date 17:11 2020/3/28
 */
public class BitcaskBuilder {

    //<editor-fold desc="静态内部类单例">
    private BitcaskBuilder(){}

    private static class BitcaskBuilderHolder{
        private static final BitcaskBuilder instance = new BitcaskBuilder();
    }

    public static BitcaskBuilder getInstance(){
        return BitcaskBuilderHolder.instance;
    }
    //</editor-fold>

    public static Bitcask newBitcask(){
        return new Bitcask(BucketManager.newInstance(), IndexMap.getInstance(), BucketSerializeCategory.BYTES);
    }

    public static Bitcask newBitcask(BucketManager bucketManager){
        return new Bitcask(bucketManager, IndexMap.getInstance(), BucketSerializeCategory.BYTES);
    }
}