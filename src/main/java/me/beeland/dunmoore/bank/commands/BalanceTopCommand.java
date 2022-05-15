package me.beeland.dunmoore.bank.commands;

import com.google.common.collect.Maps;
import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class BalanceTopCommand implements CommandExecutor {

    private GoldBank plugin;

    public BalanceTopCommand(GoldBank plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!sender.hasPermission("goldbank.balancetop")) {
            sender.sendMessage(plugin.getMessage("Lang.No-Permission"));
            return true;
        }

        int max = plugin.getConfigInteger("Options.Balance-Top-Amount");
        StringBuilder highestBalances = new StringBuilder();

        try {

            PreparedStatement statement = plugin.getDatabaseHandler().prepareStatement("SELECT name, balance FROM `dbank_balances` ORDER BY balance DESC LIMIT ?;");

            statement.setInt(1, max);
            ResultSet set = statement.executeQuery();


            while(set.next()) {

                highestBalances.append(plugin.getMessage("Lang.Balance-Top-Line")
                        .replace("%player%", set.getString("name"))
                        .replace("%amount%", set.getInt("balance") + ""))
                        .append("\n");

            }

            List<String> finalMessage = plugin.asColorizedList(plugin.getConfiguration().getConfig().getStringList("Lang.Balance-Top-Format"));

            finalMessage.forEach(line -> {
                sender.sendMessage(line.replace("%top_players%", highestBalances));
            });

            set.close();
            statement.close();

            return true;

        } catch (SQLException e) {
            sender.sendMessage(plugin.getMessage("Lang.Database-Error"));
            e.printStackTrace();
            return true;
        }

    }
}
