package com.sk89q.craftbook.bukkit;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.sk89q.craftbook.util.events.BlockPowerEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RedstonePowerListener implements Listener {

    private static final Map<NamespacedKey, Predicate<Block>> listenerRegistry = new HashMap<>();

    public static void addListener(NamespacedKey key, Predicate<Block> predicate) {
        listenerRegistry.put(key, predicate);
    }

    private final Set<Block> poweredBlocks = new HashSet<>();

    private final Set<Block> toCheck = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent event) {
        final Block block = event.getBlock();
        toCheck.add(block);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRedstone(BlockRedstoneEvent event) {
        Block source = event.getBlock();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    // Accounts for edge case of block being 2 blocks under redstone dust
                    int yOffset = (x == 0 && y == 1 && z == 0) ? -2 : y;
                    Block block = source.getRelative(x, yOffset, z);
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
        Set<NamespacedKey> keys = listenerRegistry.entrySet().stream()
                .filter(e -> e.getValue().test(block))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        boolean isPowered = block.isBlockIndirectlyPowered();
        boolean wasPowered = poweredBlocks.contains(block);
        if (wasPowered == isPowered) return;
        Event bpe = new BlockPowerEvent(block, isPowered, keys);
        CraftBookPlugin.inst().getServer().getPluginManager().callEvent(bpe);
        if (isPowered)
            poweredBlocks.add(block);
        else
            poweredBlocks.remove(block);
    }
}
