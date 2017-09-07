package org.flauschhaus.goldrushchicken;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.inventory.ItemStack;

class Delayed
{
  private ItemStack[] itemStacks;
  private Location location;

  Delayed (ItemStack[] itemStacks, Location location)
  {
    this.itemStacks = itemStacks;
    this.location = location;
  }

  boolean spawn (CraftWorld world)
  {
    return GoldRushChicken.spawn (location, itemStacks, (CraftWorld) world);
  }
}

