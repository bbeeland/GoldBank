package me.beeland.dunmoore.bank.listener;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private GoldBank plugin;

    public PlayerInteractListener(GoldBank plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerInteract(PlayerInteractEvent e) {

        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();

        if(!(e.getAction() == Action.RIGHT_CLICK_AIR) && !(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if(!plugin.getBankNoteHandler().isBanknote(item)) return;

        EconomyProfile profile = plugin.getProfileHandler().getByPlayer(e.getPlayer());
        int banknoteValue = plugin.getBankNoteHandler().getBanknoteAmount(item);

        e.getPlayer().getInventory().remove(item);
        profile.addBalance(banknoteValue);

        e.getPlayer().sendMessage(plugin.getMessage("Lang.Banknote-Deposited")
                .replace("%amount%", banknoteValue + ""));

    }

}
