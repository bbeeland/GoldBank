package me.beeland.dunmoore.bank.commands;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import me.beeland.dunmoore.bank.handler.ProfileHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DepositCommand implements CommandExecutor {

    private GoldBank plugin;
    private ProfileHandler profileHandler;

    public DepositCommand(GoldBank plugin) {
        this.plugin = plugin;
        this.profileHandler = plugin.getProfileHandler();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Sender"));
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("goldbank.deposit")) {
            sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
            return true;
        }

        // MUST PROVIDE VALID AMOUNT
        if(args.length == 0) {
            sender.sendMessage(plugin.getMessage("Lang.Invalid-Amount"));
            return true;
        }

        if(args.length == 1) {

            if(!args[0].equalsIgnoreCase("all")) {

                try {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(plugin.getMessage("Lang.Invalid-Amount"));
                    return true;
                }

            }

            EconomyProfile profile = plugin.getProfileHandler().getByPlayer(player);
            int inventoryValue = profile.getInventoryValue();
            int depositAmount = args[0].equalsIgnoreCase("all") ? inventoryValue : Integer.parseInt(args[0]);

            if(depositAmount > inventoryValue) {
                sender.sendMessage(plugin.getMessage("Lang.Insufficient-Funds"));
                return true;
            }

            if(args[0].equalsIgnoreCase("all")) {

                profile.depositAll();

                player.sendMessage(plugin.getMessage("Lang.Deposit-Success")
                        .replace("%amount%", inventoryValue + ""));

                return true;
            }

            profile.deposit(depositAmount);
            sender.sendMessage(plugin.getMessage("Lang.Deposit-Success")
                    .replace("%amount%", "" + depositAmount));

            return true;
        }


        return true;
    }
}
