package com.moulberry.axiom;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class AxiomPlaceholderExpansion extends PlaceholderExpansion {
    private final AxiomPaper plugin; //

    public AxiomPlaceholderExpansion(AxiomPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "axiom";
    }

    @Override
    public @NotNull String getAuthor() {
        return "mryd";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (params.equals("can_use_axiom")) {
            plugin.canUseAxiom(player.getPlayer());
        }
        return null;
    }

}
