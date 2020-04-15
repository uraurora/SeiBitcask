package config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import util.FileUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

final class YamlConfig {

    private YamlConfig(){}

    /**
     * 功能描述:
     * 〈加载yml文件〉
     */
    public static Map<?, ?> loadYaml(String fileName) {
        InputStream in = YamlConfig.class.getClassLoader().getResourceAsStream(fileName);
        return FileUtil.isEmpty(fileName) ? (LinkedHashMap<?, ?>) new Yaml().load(in) : null;
    }

    public static <T> T loadYaml(String fileName, Class<T> clazz) {
        InputStream in = YamlConfig.class.getClassLoader().getResourceAsStream(fileName);
        return FileUtil.isEmpty(fileName) ? new Yaml().loadAs(in, clazz) : null;
    }

    /**
     * 功能描述:
     * 〈往yml文件中写数据,数据为map〉
     */
    public static void dumpYaml(String fileName, Map<?, ?> map) throws IOException {
        if (FileUtil.isEmpty(fileName)) {
            FileWriter fileWriter = new FileWriter(YamlConfig.class.getResource(fileName).getFile());
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(map, fileWriter);
        }
    }

    /**
     * 功能描述:
     * 〈根据key查询yml中的数据〉
     */
    public static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!"".equals(input)) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1);
                    return getProperty((Map<?, ?>) map.get(left), right);
                } else {
                    return map.getOrDefault(input, null);
                }
            }
        }
        return null;
    }

    /**
     * 功能描述:
     * 〈设置yml中的值〉
     */
    @SuppressWarnings("unchecked")
    public static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!"".equals(input)) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1);
                    setProperty((Map<?, ?>) map.get(left), right, value);
                } else {
                    ((Map<Object, Object>) map).put(qualifiedKey, value);
                }
            }
        }
    }
}
