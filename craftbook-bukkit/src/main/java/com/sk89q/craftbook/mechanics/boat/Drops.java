/*
 * CraftBook Copyright (C) me4502 <https://matthewmiller.dev/>
 * CraftBook Copyright (C) EngineHub and Contributors <https://enginehub.org/>
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

package com.sk89q.craftbook.mechanics.boat;

import com.sk89q.craftbook.AbstractCraftBookMechanic;
import com.sk89q.craftbook.util.EventUtil;
import com.sk89q.craftbook.util.ItemUtil;
import com.sk89q.util.yaml.YAMLProcessor;
import org.bukkit.entity.Boat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.ItemStack;

public class Drops extends AbstractCraftBookMechanic {

    @EventHandler(priority = EventPriority.HIGH)
    public void onVehicleDestroy(VehicleDestroyEvent event) {

        if(!EventUtil.passesFilter(event)) return;

        if (!(event.getVehicle() instanceof Boat)) return;

        if (event.getAttacker() == null) {
            Boat boat = (Boat) event.getVehicle();
            boat.getLocation().getWorld().dropItemNaturally(boat.getLocation(), new ItemStack(ItemUtil.getBoatFromTree(boat.getWoodType())));
            boat.remove();
            event.setCancelled(true);
        }
    }

    @Override
    public void loadConfiguration (YAMLProcessor config, String path) {

    }
}