package me.beeland.dunmoore.bank.handler;

import me.beeland.dunmoore.bank.EconomyProfile;
import me.beeland.dunmoore.bank.GoldBank;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class VaultHandler implements Economy {

    private GoldBank plugin;
    private NumberFormat currencyFormat;

    public VaultHandler(GoldBank plugin) {
        this.plugin = plugin;
        this.currencyFormat = NumberFormat.getCurrencyInstance();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "GoldBank";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return currencyFormat.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "Gold";
    }

    @Override
    public String currencyNameSingular() {
        return "Gold";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return Bukkit.getOfflinePlayer(playerName).hasPlayedBefore();
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return player.hasPlayedBefore();
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return player.hasPlayedBefore();
    }

    @Override
    public double getBalance(String playerName) {
        return plugin.getProfileHandler().getByName(playerName).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player.getName());
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player.getName()) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return getBalance(player.getName()) > amount;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {

        EconomyProfile profile = plugin.getProfileHandler().getByName(playerName);

        if(profile == null) return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Profile not found.");

        try {
            Integer.parseInt((int) amount + "");
        } catch (NumberFormatException e) {
            return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Must be a valid integer");
        }
        if(amount < 0) return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Invalid withdrawal amount");
        if(profile.getBalance() < amount) return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        profile.removeBalance((int) amount);
        return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {

        EconomyProfile profile = plugin.getProfileHandler().getByName(playerName);

        try {
            Integer.parseInt((int) amount + "");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if(profile == null) return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Profile not found!");
        if(amount <= 0) return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.FAILURE, "Invalid deposit amount");

        profile.addBalance((int) amount);
        return new EconomyResponse(amount, profile.getBalance(), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
