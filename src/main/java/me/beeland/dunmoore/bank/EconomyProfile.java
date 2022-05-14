package me.beeland.dunmoore.bank;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

public class EconomyProfile {

    private GoldBank plugin;

    private UUID owner;
    private Player player;
    private int balance;
    private boolean aged;

    private String playerName;

    public EconomyProfile(GoldBank plugin, UUID owner, int balance, boolean aged) {

        this.aged = aged;
        this.plugin = plugin;
        this.player = Bukkit.getPlayer(owner);
        this.owner = owner;
        this.balance = balance;

        this.playerName = getPlayer().getName();
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isAged() {
        return aged;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(owner);
    }

    public int getBalance() {
        return balance;
    }

    public void addBalance(int amount) {
        this.balance += amount;
    }

    public void removeBalance(int amount) {
        this.balance -= amount;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void withdraw(int amount) {

        Player player = getPlayer();

        if(amount > balance) {
            player.sendMessage(plugin.getMessage(""));
        }

        int blocks = (int) Math.floor(amount / 81);
        int ingots = (int) Math.floor(((amount - (blocks * 81)) / 9));
        int nuggets = (int) Math.floor( (amount - ((blocks * 81) + (ingots * 9))));

        player.getInventory().addItem(
                new ItemStack(Material.GOLD_NUGGET, nuggets),
                new ItemStack(Material.GOLD_INGOT, ingots),
                new ItemStack(Material.GOLD_BLOCK, blocks));

        balance -= amount;

    }

    public void withdrawAll() {
        withdraw(balance);
    }

    public void deposit(int amount) {

        int value = getInventoryValue() - amount;

        for(ItemStack item : player.getInventory()) {

            if(item == null || item.getType() == Material.AIR) continue;

            switch(item.getType()) {
                case GOLD_BLOCK, GOLD_INGOT, GOLD_NUGGET -> item.setAmount(0);
            }
        }

        int blocks = (int) Math.floor(value / 81);
        int ingots = (int) Math.floor(((value - (blocks * 81)) / 9));
        int nuggets = (int) Math.floor( (value - ((blocks * 81) + (ingots * 9))));

        player.getInventory().addItem(
                new ItemStack(Material.GOLD_BLOCK, blocks),
                new ItemStack(Material.GOLD_INGOT, ingots),
                new ItemStack(Material.GOLD_NUGGET, nuggets));

        balance += amount;
    }

    public void payPlayer(EconomyProfile target, int amount) {

        if(balance < amount) return;

        target.addBalance(amount);
        balance -= amount;
    }

    public void depositAll() {
        deposit(getInventoryValue());
    }

    public void depositHand() {
        deposit(getHandValue());
    }


    public int getHandValue() {

        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();

        int itemValue = plugin.getValue(item.getType());
        int inventoryValue = 0;



        for(ItemStack inventoryItem : inventory) {

            if(plugin.isValidMaterial(inventoryItem.getType())) {

                inventoryValue += (itemValue * inventoryItem.getAmount());
            }
        }
        return inventoryValue;
    }

    public int getInventoryValue() {

        PlayerInventory inventory = player.getInventory();
        int calculatedValue = 0;

        for(ItemStack item : inventory) {

            if(item == null || item.getType() == Material.AIR) continue;

            switch(item.getType()) {
                case GOLD_BLOCK -> calculatedValue += (81 * item.getAmount());
                case GOLD_INGOT -> calculatedValue += (9 * item.getAmount());
                case GOLD_NUGGET -> calculatedValue += item.getAmount();
            }
        }
        return calculatedValue;
    }

}
