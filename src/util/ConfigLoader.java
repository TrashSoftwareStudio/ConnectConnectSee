package util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

    private static final String configFileName = "pref.ini";

    public static void writeConfig(String item, String content) {
        HashMap<String, String> cfg = readAllConfigs();
        cfg.put(item, content);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(configFileName));
            for (Map.Entry<String, String> entry : cfg.entrySet()) {
                bw.write(entry.getKey());
                bw.write("=");
                bw.write(entry.getValue());
                bw.write('\n');
            }
            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String getConfig(String key) {
        return readAllConfigs().get(key);
    }

    private static HashMap<String, String> readAllConfigs() {
        HashMap<String, String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("=");
                map.put(split[0], split[1]);
            }
            br.close();
        } catch (IOException e) {
            //
        }
        return map;
    }
}
