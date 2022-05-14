package me.beeland.dunmoore.bank.handler;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProfileHandler {

    private GoldBank plugin;
    private Set<EconomyProfile> profiles;

    public ProfileHandler(GoldBank plugin) {
        this.plugin = plugin;
        this.profiles = new HashSet<>();
    }

    public Set<EconomyProfile> getProfiles() {
        return profiles;
    }

    public void init() {

        if(Bukkit.getOnlinePlayers().size() == 0) return;

        for(Player player : Bukkit.getOnlinePlayers()) {
            loadProfile(player.getUniqueId());
        }

    }

    public void save() {
        getProfiles().forEach(profile -> saveProfile(profile));
    }

    public EconomyProfile getByPlayer(Player player) {
        return getByUUID(player.getUniqueId());
    }

    public EconomyProfile getByUUID(UUID uuid) {

        for(EconomyProfile profile : profiles) {
            if(profile.getOwner().toString().equalsIgnoreCase(uuid.toString())) return profile;
        }

        throw new IllegalArgumentException("Given UUID is not a loaded profile!");
    }

    public void loadProfile(UUID owner) {

        try {

            PreparedStatement statement = plugin.getDatabaseHandler().prepareStatement("SELECT uuid,balance FROM dbank_balances WHERE uuid=?");

            statement.setString(1, owner.toString());

            ResultSet set = statement.executeQuery();

            // If user has account
            if(set.next()) {

                profiles.add(new EconomyProfile(
                        plugin,
                        UUID.fromString(set.getString("uuid")),
                        set.getInt("balance"), true));

            } else {

                profiles.add(new EconomyProfile(plugin, owner, plugin.getConfigInteger("Options.Default-Balance"), false));

            }

            statement.close();
            set.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveProfile(UUID uuid) {
        saveProfile(getByUUID(uuid));
    }

    public void saveProfile(EconomyProfile profile) {

        try {

            PreparedStatement statement;

            if(profile.isAged()) {
                //Update
                statement = plugin.getDatabaseHandler().prepareStatement("UPDATE dbank_balances SET balance = ? WHERE uuid = ?");

                statement.setInt(1, profile.getBalance());
                statement.setString(2, profile.getOwner().toString());

            } else {
                //Insert
                statement = plugin.getDatabaseHandler().prepareStatement("INSERT INTO dbank_balances(uuid, name, balance) VALUES(?, ?, ?)");

                statement.setString(1, profile.getOwner().toString());
                statement.setString(2, profile.getPlayerName());
                statement.setInt(3, profile.getBalance());
            }

            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
