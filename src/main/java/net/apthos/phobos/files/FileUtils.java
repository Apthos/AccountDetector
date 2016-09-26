package net.apthos.phobos.files;

import net.apthos.phobos.AccountDetector;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileUtils {

    public static void createFiles() {
        File SQLFile = new File(AccountDetector.getInstance().getDataFolder()
                + "/MySQL.yml");
        if (!SQLFile.exists()) {
            AccountDetector.getInstance().saveResource("MySQL.yml", false);
        }
    }

    public static String[] getLoginData() {
        File SQLFile = new File(AccountDetector.getInstance().getDataFolder()
                + "/MySQL.yml");
        if (!SQLFile.exists()) {
            createFiles();
        }
        YamlConfiguration Yaml = YamlConfiguration.loadConfiguration(SQLFile);

        String data[] = {Yaml.getString("Host"), Yaml.getString("Port"),
                Yaml.getString("Database"), Yaml.getString("User"),
                Yaml.getString("Pass")};
        return data;
    }

}