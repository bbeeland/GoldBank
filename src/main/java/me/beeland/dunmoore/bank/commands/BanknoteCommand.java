package me.beeland.dunmoore.bank.commands;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BanknoteCommand implements CommandExecutor {

    private GoldBank plugin;

    public BanknoteCommand(GoldBank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Sender"));
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("goldbank.banknote")) {
            sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
            return true;
        }

        if(args.length == 0 || args.length > 1) {
            sender.sendMessage(plugin.getMessage("Lang.Banknote-Usage"));
            return true;
        }

        if(args.length == 1) {

            try {
                Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessage("Lang.Invalid-Amount"));
                return true;
            }

            EconomyProfile profile = plugin.getProfileHandler().getByPlayer(player);
            int banknoteAmount = Integer.parseInt(args[0]);
            int maxAmount = plugin.getConfigInteger("Options.Bank-Note.Max-Amount");

            if(banknoteAmount > maxAmount) {
                sender.sendMessage(plugin.getMessage("Lang.Banknote-Exceeded"));
                return true;
            }

            if(profile.getBalance() < banknoteAmount) {
                sender.sendMessage(plugin.getMessage("Lang.Insufficient-Funds"));
                return true;
            }

            ItemStack item = plugin.getBankNoteHandler().createBanknote(banknoteAmount);
            profile.removeBalance(banknoteAmount);

            if(player.getInventory().firstEmpty() == -1) {
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            } else {
                player.getInventory().addItem(item);
            }

            sender.sendMessage(plugin.getMessage("Lang.Banknote-Created")
                    .replace("%amount%", banknoteAmount + ""));

        }

        return true;
    }
}
