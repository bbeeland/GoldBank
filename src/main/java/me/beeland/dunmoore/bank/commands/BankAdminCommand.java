package me.beeland.dunmoore.bank.commands;

import com.google.common.collect.Lists;
import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import me.beeland.dunmoore.bank.handler.ProfileHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BankAdminCommand implements CommandExecutor, TabCompleter {

    private GoldBank plugin;
    private ProfileHandler profileHandler;

    public BankAdminCommand(GoldBank plugin) {
        this.plugin = plugin;
        this.profileHandler = plugin.getProfileHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("goldbank.admin")) {
            sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
            return true;
        }

        if(args.length < 3) {

            if(args.length == 0) {
                sender.sendMessage(plugin.getMessage("Lang.Admin-Usage"));
                return true;
            }

            if(args.length == 1) {

                if(!args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(plugin.getMessage("Lang.Reload-Usage"));
                    return true;
                }

                plugin.getConfiguration().reloadConfig();
                sender.sendMessage(plugin.getMessage("Lang.Plugin-Reloaded"));

                return true;
            }

            if(!args[0].equalsIgnoreCase("give") && !args[0].equalsIgnoreCase("take") && !args[0].equalsIgnoreCase("set")) {
                sender.sendMessage(plugin.getMessage("Lang.Admin-Usage"));
                return true;
            }
        }

        if(args.length == 3) {

            if(Bukkit.getPlayer(args[1]) == null) {
                sender.sendMessage(plugin.getMessage("Lang.Invalid-Target"));
                return true;
            }

            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessage("Lang.Invalid-Amount"));
            }

            Player target = Bukkit.getPlayer(args[1]);
            EconomyProfile profile = profileHandler.getByPlayer(target);
            int amount = Integer.parseInt(args[2]);

            if(args[0].equalsIgnoreCase("give")) {

                if(!sender.hasPermission("goldbank.admin.give")) {
                    sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
                    return true;
                }

                profile.addBalance(amount);

                sender.sendMessage(plugin.getMessage("Lang.Admin-Give")
                        .replace("%target%", target.getName())
                        .replace("%amount%", amount + ""));

                target.sendMessage(plugin.getMessage("Lang.Money-Deposited")
                        .replace("%amount%", amount + ""));

                return true;

            }

            if(args[0].equalsIgnoreCase("take")) {

                if(!sender.hasPermission("goldbank.admin.take")) {
                    sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
                    return true;
                }

                if(profile.getBalance() < amount) {
                    sender.sendMessage(plugin.getMessage("Lang.Admin-Insufficient-Funds")
                            .replace("%amount%", amount + ""));
                    return true;
                }

                profile.removeBalance(amount);

                sender.sendMessage(plugin.getMessage("Lang.Admin-Take")
                        .replace("%target%", target.getName())
                        .replace("%amount%", amount + ""));

                target.sendMessage(plugin.getMessage("Lang.Money-Withdrawn")
                        .replace("%amount%", amount + ""));

                return true;
            }

            if(args[0].equalsIgnoreCase("set")) {

                if(!sender.hasPermission("goldbank.admin.set")) {
                    sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
                    return true;
                }

                profile.setBalance(amount);

                sender.sendMessage(plugin.getMessage("Lang.Admin-Set")
                        .replace("%target%", target.getName())
                        .replace("%amount%", amount + ""));

                target.sendMessage(plugin.getMessage("Lang.Money-Set")
                        .replace("%amount%", amount + ""));

            }

        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("goldbank.admin")) return null;

        if(args.length == 1) {

            List<String> values = Lists.newArrayList();
            List<String> end = Lists.newArrayList();

            if(sender.hasPermission("goldbank.admin.give")) values.add("give");
            if(sender.hasPermission("goldbank.admin.take")) values.add("take");
            if(sender.hasPermission("goldbank.admin.set")) values.add("set");
            if(sender.hasPermission("goldbank.admin.reload")) values.add("reload");

            values.forEach(line -> {
                if(line.startsWith(args[0])) end.add(line);
            });

            values.stream().filter(s -> s.startsWith(args[0]));
            return end;
        }

        if(args.length == 2) {

            List<String> values = Lists.newArrayList();

            for(Player player : Bukkit.getOnlinePlayers()) {

                if(player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                    values.add(player.getName());
                }

            }

            return values;
        }

        return null;
    }
}
