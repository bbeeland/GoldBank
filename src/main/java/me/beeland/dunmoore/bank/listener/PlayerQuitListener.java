package me.beeland.dunmoore.bank.listener;

import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private GoldBank plugin;

    public PlayerQuitListener(GoldBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerQuit(PlayerQuitEvent e) {

        plugin.getProfileHandler().saveProfile(e.getPlayer().getUniqueId());

    }

}
