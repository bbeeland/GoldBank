package me.beeland.dunmoore.bank.commands;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import me.beeland.dunmoore.bank.handler.ProfileHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommand implements CommandExecutor {

    private GoldBank plugin;
    private ProfileHandler profileHandler;

    public PayCommand(GoldBank plugin) {
        this.plugin = plugin;
        this.profileHandler = plugin.getProfileHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Sender"));
            return true;
        }

        if(!sender.hasPermission("goldbank.pay")) {
            sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
            return true;
        }

        if(args.length < 2 || args.length > 2) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Pay-Arguments"));
            return true;
        }

        Player player = (Player) sender;
        Player target = plugin.getServer().getPlayer(args[0]);

        if(target == null) {
            player.sendMessage(plugin.getMessage("Lang.Invalid-Target"));
            return true;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Amount"));
        }

        EconomyProfile playerProfile = profileHandler.getByPlayer(player);
        EconomyProfile targetProfile = profileHandler.getByPlayer(target);
        int paymentAmount = Integer.parseInt(args[1]);

        playerProfile.payPlayer(targetProfile, paymentAmount);

        player.sendMessage(plugin.getMessage("Lang.Pay-Success")
                .replace("%target%", target.getName())
                .replace("%amount%", paymentAmount + ""));

        target.sendMessage(plugin.getMessage("Lang.Payed-Success")
                .replace("%source%", player.getName())
                .replace("%amount%", paymentAmount + ""));

        return true;
    }
}
