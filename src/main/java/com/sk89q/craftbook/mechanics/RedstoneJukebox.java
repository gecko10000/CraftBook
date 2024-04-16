package com.sk89q.craftbook.mechanics;

import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import com.sk89q.craftbook.bukkit.RedstonePowerListener;
import com.sk89q.craftbook.util.events.BlockPowerEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.event.EventHandler;

import com.sk89q.craftbook.AbstractCraftBookMechanic;
import com.sk89q.craftbook.util.events.SourcedBlockRedstoneEvent;
import com.sk89q.util.yaml.YAMLProcessor;

public class RedstoneJukebox extends AbstractCraftBookMechanic {

    private NamespacedKey jukeboxKey = new NamespacedKey(CraftBookPlugin.inst(), "jukebox");

    public RedstoneJukebox() {
        RedstonePowerListener.addListener(jukeboxKey, b -> b.getType() == Material.JUKEBOX);
    }

    @EventHandler
    public void onRedstonePower(BlockPowerEvent event) {

        if(!event.hasKey(jukeboxKey)) return;
        Jukebox juke = (org.bukkit.block.Jukebox) event.getBlock().getState(false);
        if (!juke.hasRecord()) return;
        juke.stopPlaying();
        if (event.on) {
            juke.setPlaying(juke.getPlaying());
            juke.update();
        }
    }

    @Override
    public void loadConfiguration (YAMLProcessor config, String path) {
    }
}
