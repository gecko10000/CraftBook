package com.sk89q.craftbook.bukkit;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.sk89q.craftbook.util.events.BlockPowerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StickyPistonPowerListener implements Listener {

    private final Set<Block> poweredBlocks = new HashSet<>();

    private final Set<Block> toCheck = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent event) {
        final Block block = event.getBlock();
        toCheck.add(block);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRedstone(BlockRedstoneEvent event) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = event.getBlock().getRelative(x, y, z);
                    toCheck.add(block);
                }
            }
        }
    }

    @EventHandler
    public void onTick(ServerTickEndEvent event) {
        toCheck.forEach(this::check);
        toCheck.clear();
    }

    private void check(Block block) {
        if (block.getType() != Material.STICKY_PISTON) return;
        boolean isPowered = block.isBlockIndirectlyPowered();
        boolean wasPowered = poweredBlocks.contains(block);
        if (wasPowered == isPowered) return;
        Event bpe = new BlockPowerEvent(block, isPowered);
        CraftBookPlugin.inst().getServer().getPluginManager().callEvent(bpe);
        if (isPowered)
            poweredBlocks.add(block);
        else
            poweredBlocks.remove(block);
    }
}
