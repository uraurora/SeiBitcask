package config;

public class BitcaskConfig {

    private int keyMaxSize;

    private int valueMaxSize;

    public int getKeyMaxSize() {
        return keyMaxSize;
    }

    public void setKeyMaxSize(int keyMaxSize) {
        this.keyMaxSize = keyMaxSize;
    }

    public int getValueMaxSize() {
        return valueMaxSize;
    }

    public void setValueMaxSize(int valueMaxSize) {
        this.valueMaxSize = valueMaxSize;
    }

    @Override
    public String toString() {
        return "BitcaskConfig{" +
                ", keyMaxSize=" + keyMaxSize +
                ", valueMaxSize=" + valueMaxSize +
                '}';
    }
}
