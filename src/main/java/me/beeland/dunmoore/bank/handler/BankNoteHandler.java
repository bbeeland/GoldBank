package me.beeland.dunmoore.bank.handler;

import com.google.common.collect.Lists;
import me.beeland.dunmoore.bank.GoldBank;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BankNoteHandler {

    private GoldBank plugin;
    private ProfileHandler profileHandler;
    private Pattern moneyPattern;
    private ItemStack base;

    public BankNoteHandler(GoldBank plugin) {

        this.plugin = plugin;
        this.profileHandler = plugin.getProfileHandler();
        this.moneyPattern = Pattern.compile("((([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?\\d?\\d?)?$)");

        Material baseType = Material.getMaterial(plugin.getConfigValue("Options.Bank-Note.Material"));
        String displayName = plugin.getMessage("Options.Bank-Note.Display-Name");
        List<String> lore = plugin.asColorizedList(plugin.getConfiguration().getConfig().getStringList("Options.Bank-Note.Lore"));

        this.base = new ItemStack(baseType);

        ItemMeta baseMeta = this.base.getItemMeta();
        baseMeta.setDisplayName(displayName);
        baseMeta.setLore(lore);

        base.setItemMeta(baseMeta);

    }

    public ItemStack createBanknote(int value) {

        ItemStack banknote = new ItemStack(base.getType());
        ItemMeta bankNoteMeta = banknote.getItemMeta();
        List<String> lore = Lists.newArrayList();

        bankNoteMeta.addEnchant(Enchantment.values()[new Random().nextInt(Enchantment.values().length)], 1, true);
        bankNoteMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        bankNoteMeta.setDisplayName(base.getItemMeta().getDisplayName());

        base.getItemMeta().getLore().forEach(line -> {
            lore.add(line.replace("%amount%", value + ""));
        });

        bankNoteMeta.setLore(lore);
        banknote.setItemMeta(bankNoteMeta);

        return banknote;
    }

    public boolean isBanknote(ItemStack item) {

        ItemMeta itemMeta = item.getItemMeta();
        ItemMeta baseMeta = base.getItemMeta();

        if(item.getType() != base.getType()) return false;
        if(item.getEnchantments().isEmpty()) return false;
        if(!itemMeta.getDisplayName().equals(baseMeta.getDisplayName())) return false;
        if(!(itemMeta.getLore().size() == baseMeta.getLore().size())) return false;

        return true;
    }

    public int getBanknoteAmount(ItemStack item) {

        if(!isBanknote(item)) return 0;

        for(String line : item.getItemMeta().getLore()) {

            Matcher matcher = moneyPattern.matcher(line);

            if(matcher.find()) {

                String amount = matcher.group(1);

                return Integer.parseInt(amount);
            }

        }

        return 0;
    }

}
