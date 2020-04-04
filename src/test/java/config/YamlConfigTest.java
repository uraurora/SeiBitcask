package config;

import org.junit.Test;

import java.io.FileNotFoundException;

public class YamlConfigTest {


    @Test
    public void loadYaml() {
        System.out.println(YamlConfig.loadYaml("properties.yml"));
    }

    @Test
    public void dumpYaml() {
    }

    @Test
    public void getProperty() {
    }

    @Test
    public void setProperty() {
    }
}