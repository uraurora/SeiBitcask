package core.db;

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




}