package me.beeland.dunmoore.bank.commands;

import me.beeland.dunmoore.bank.GoldBank;
import me.beeland.dunmoore.bank.handler.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private GoldBank plugin;
    private ProfileHandler profileHandler;

    public BalanceCommand(GoldBank plugin) {
        this.plugin = plugin;
        this.profileHandler = plugin.getProfileHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Sender"));
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0) {

            if(!player.hasPermission("goldbank.balance.self")) {
                sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
                return true;
            }

            player.sendMessage(plugin.withPlaceholders(player, plugin.getMessage("Lang.Self-Balance")));
            return true;
        }

        if(args.length == 1) {

            if(!player.hasPermission("goldbank.balance.others")) {
                sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
                return true;
            }

            if(Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(plugin.getMessage("Lang.Invalid-Target"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            String messageWithPlaceholders = plugin.getMessage("Lang.Target-Balance")
                    .replace("%target_name%", target.getName())
                    .replace("%target_balance%", "" + profileHandler.getByPlayer(target).getBalance());

            player.sendMessage(plugin.withPlaceholders(player, messageWithPlaceholders));
            return true;
        }

        return true;
    }
}
