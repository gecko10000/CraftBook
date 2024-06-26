// $Id$
/*
 * CraftBook Copyright (C) 2010 sk89q <http://www.sk89q.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.craftbook.mechanics;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.sk89q.craftbook.AbstractCraftBookMechanic;
import com.sk89q.craftbook.util.EventUtil;
import com.sk89q.craftbook.util.events.SourcedBlockRedstoneEvent;
import com.sk89q.util.yaml.YAMLProcessor;
import org.bukkit.inventory.EquipmentSlot;

/**
 * This mechanism allow players to toggle the fire on top of Netherrack.
 *
 * @author sk89q
 */
public class Netherrack extends AbstractCraftBookMechanic {

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockRedstoneChange(SourcedBlockRedstoneEvent event) {

        if(!EventUtil.passesFilter(event)) return;

        if(event.isMinor())
            return;

        Block block = event.getBlock();
        Material type = block.getType();
        if(type != Material.NETHERRACK && type != Material.SOUL_SOIL) return;

        Block above = event.getBlock().getRelative(0, 1, 0);

        if (event.isOn() && canReplaceWithFire(above.getType())) {
            Material fire = type == Material.NETHERRACK ? Material.FIRE : Material.SOUL_FIRE;
            above.setType(fire);
        } else if (!event.isOn() && (above.getType() == Material.FIRE || above.getType() == Material.SOUL_FIRE)) {
            above.setType(Material.AIR);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeftClick(PlayerInteractEvent event) {

        if(!EventUtil.passesFilter(event)) return;

        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getHand() != EquipmentSlot.HAND) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        if(block.getType() != Material.NETHERRACK && block.getType() != Material.SOUL_SOIL) return;
        if (event.getBlockFace() == BlockFace.UP) {
            Block fire = event.getClickedBlock().getRelative(event.getBlockFace());
            if ((fire.getType() == Material.FIRE || fire.getType() == Material.SOUL_FIRE) && fire.getRelative(BlockFace.DOWN).isBlockPowered()) {
                event.setCancelled(true);
            }
        }
    }

    private static boolean canReplaceWithFire(Material t) {

        switch (t) {
            case SNOW:
            case SHORT_GRASS:
            case VINE:
            case DEAD_BUSH:
            case AIR:
            case VOID_AIR:
            case CAVE_AIR:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void loadConfiguration (YAMLProcessor config, String path) {

    }
}
