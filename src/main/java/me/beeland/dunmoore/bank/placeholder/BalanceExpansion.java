package me.beeland.dunmoore.bank.placeholder;

import me.beeland.dunmoore.bank.handler.ProfileHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class BalanceExpansion extends PlaceholderExpansion {

    private ProfileHandler profileHandler;

    public BalanceExpansion(ProfileHandler profileHandler) {
        this.profileHandler = profileHandler;
    }

    @Override
    public String getIdentifier() {
        return "goldbank-balance";
    }

    @Override
    public String getAuthor() {
        return "Britton B";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        return "" + profileHandler.getByUUID(player.getUniqueId()).getBalance();
    }
}
