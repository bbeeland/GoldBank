package me.beeland.dunmoore.bank.listener;

import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private GoldBank plugin;

    public PlayerJoinListener(GoldBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    void onPlayerJoin(PlayerJoinEvent e) {

        plugin.getProfileHandler().loadProfile(e.getPlayer().getUniqueId());

    }

}
