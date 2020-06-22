package core.db;

import core.constant.BucketSerializeCategory;
import file.entity.IndexEntry;
import file.manager.IIndexMap;
import file.manager.IndexMap;

/**
 * @author : gaoxiaodong04
 * @program : SeiBitcask
 * @date : 2020-06-19 10:28
 * @description : bitcask的创建工厂
 */
public class BitcaskFactory {

    private BitcaskFactory(){}

    public static Bitcask newInstance(){
        return new Bitcask(IndexMap.getInstance(), BucketSerializeCategory.DEFAULT);
    }

    public static Bitcask newInstance(IIndexMap<String, IndexEntry> indexMap){
        return new Bitcask(indexMap, BucketSerializeCategory.DEFAULT);
    }

}
