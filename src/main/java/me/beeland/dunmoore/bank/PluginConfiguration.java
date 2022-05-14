package me.beeland.dunmoore.bank;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginConfiguration {

    private File file;
    private FileConfiguration config;

    public PluginConfiguration(GoldBank plugin, File directory, String filename, boolean copy) {

        this.file = new File(directory, filename);

        if(!directory.exists()) directory.mkdir();

        if(!file.exists()) {

            if(copy) {

                plugin.saveResource(filename, false);

            } else {

                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            plugin.getLogger().info("&c&lNew config created, disabling plugin.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);

        }

        reloadConfig();

    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            this.config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
