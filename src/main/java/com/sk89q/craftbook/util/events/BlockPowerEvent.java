package com.sk89q.craftbook.util.events;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BlockPowerEvent extends BlockEvent {

    public final boolean on;
    private final Set<NamespacedKey> keys;

    public BlockPowerEvent(Block block, boolean on, Set<NamespacedKey> keys) {
        super(block);
        this.on = on;
        this.keys = keys;
    }

    public boolean hasKey(NamespacedKey key) {
        return keys.contains(key);
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
