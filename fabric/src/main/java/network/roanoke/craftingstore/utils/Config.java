package network.roanoke.craftingstore.utils;

import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


public class Config {
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("CraftingStore/config.properties");
    private final Properties properties;

    public Config() {
        properties = new Properties();
        try {
            if (Files.exists(CONFIG_FILE_PATH)) {
                FileInputStream file = new FileInputStream(CONFIG_FILE_PATH.toFile());
                properties.load(file);
                file.close();
            } else {
                // If the file doesn't exist, create it with default values
                setApiKey("api-key-here");
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiKey() {
        return properties.getProperty("apiKey");
    }

    public void setApiKey(String apiKey) {
        properties.setProperty("apiKey", String.valueOf(apiKey));
        save();
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_FILE_PATH.getParent());

            if (!CONFIG_FILE_PATH.toFile().exists()) {
                Files.createFile(CONFIG_FILE_PATH);
            }

            try (FileOutputStream file = new FileOutputStream(CONFIG_FILE_PATH.toFile())) {
                properties.store(file, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}