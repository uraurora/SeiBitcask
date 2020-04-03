package config;

import core.db.Bitcask;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.Assert.*;

public class YamlConfigTest {


    @Test
    public void loadYaml() throws FileNotFoundException {
        File file = new File("./src/main/resources/properties.yml");
        assertTrue(file.exists());
        Yaml yaml = new Yaml();
        BitcaskConfig map = yaml.loadAs(new FileInputStream(file), BitcaskConfig.class);
        System.out.println(map);
        //System.out.println(YamlConfig.loadYaml("./src/main/resources/properties.yml"));
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