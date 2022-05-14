package me.beeland.dunmoore.bank;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.beeland.dunmoore.bank.commands.BalanceCommand;
import me.beeland.dunmoore.bank.commands.DepositCommand;
import me.beeland.dunmoore.bank.commands.PayCommand;
import me.beeland.dunmoore.bank.commands.WithdrawCommand;
import me.beeland.dunmoore.bank.handler.DatabaseHandler;
import me.beeland.dunmoore.bank.handler.ProfileHandler;
import me.beeland.dunmoore.bank.listener.PlayerJoinListener;
import me.beeland.dunmoore.bank.listener.PlayerQuitListener;
import me.beeland.dunmoore.bank.placeholder.BalanceExpansion;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoldBank extends JavaPlugin {

    private PluginConfiguration config;
    private DatabaseHandler databaseHandler;
    private ProfileHandler profileHandler;

    private HashMap<Material, Integer> values;

    private boolean rgbEnabled;
    private boolean papiEnabled;
    private Pattern hexPattern;

    @Override
    public void onEnable() {

        this.config = new PluginConfiguration(this, getDataFolder(), "config.yml", true);
        this.databaseHandler = new DatabaseHandler(getConfigValue("Database.Host"),
                getConfigInteger("Database.Port"),
                getConfigValue("Database.Name"),
                getConfigValue("Database.Username"),
                getConfigValue("Database.Password"));

        this.profileHandler = new ProfileHandler(this);
        this.values = Maps.newHashMap();

        try {

            PreparedStatement statement = databaseHandler.prepareStatement("CREATE TABLE IF NOT EXISTS dbank_balances(`uuid` VARCHAR(36) PRIMARY KEY NOT NULL, `name` VARCHAR(32) NOT NULL, `balance` INTEGER NOT NULL DEFAULT 0);");
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.papiEnabled = getConfigOption("Options.Enable-PlaceholderAPI") && (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null);
        this.rgbEnabled = getConfigOption("Options.Enable-RGB");
        this.hexPattern = Pattern.compile("#([A-Fa-f0-9]){6}>");

        if(papiEnabled) {
            new BalanceExpansion(profileHandler).register();

        }

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);

        getCommand("balance").setExecutor(new BalanceCommand(this));
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
        getCommand("deposit").setExecutor(new DepositCommand(this));
        getCommand("pay").setExecutor(new PayCommand(this));

        profileHandler.init();

        values.put(Material.GOLD_NUGGET, 1);
        values.put(Material.GOLD_INGOT, 9);
        values.put(Material.GOLD_BLOCK, 81);
    }

    @Override
    public void onDisable() {

        getProfileHandler().save();

        try {
            this.databaseHandler.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ProfileHandler getProfileHandler() {
        return profileHandler;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public String withColor(String message) {

        if(rgbEnabled) {

            Matcher matcher = hexPattern.matcher(message);

            while(matcher.find()) {

                ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length()));
                String before = message.substring(0, matcher.start());
                String after = message.substring(matcher.end());

                message = before + hexColor + after;
                matcher = hexPattern.matcher(message);
            }
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String withPlaceholders(Player player, String message) {

        if(!papiEnabled) return withColor(message);

        return PlaceholderAPI.setPlaceholders(player, withColor(message));
    }

    public List<String> asColorizedList(List<String> list) {

        List<String> coloredList = Lists.newArrayList();
        list.forEach(line -> coloredList.add(withColor(line)));
        return list;
    }

    public String getConfigValue(String path) {
        return config.getConfig().getString(path);
    }

    public boolean getConfigOption(String path) {
        return config.getConfig().getBoolean(path);
    }

    public int getConfigInteger(String path) {
        return config.getConfig().getInt(path);
    }

    public String getMessage(String path) {
        return withColor(config.getConfig().getString(path));
    }

    public boolean isValidMaterial(Material material) {
        return values.containsKey(material);
    }

    public int getValue(Material material) {
        return values.get(material);
    }

}
