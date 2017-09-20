package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GoldRushChicken extends EntityChicken implements InventoryHolder
{
  private Inventory bag;
  private static List<Delayed> delayedChicken = new ArrayList<> ();

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    setAttributes ();

    bag = Bukkit.createInventory (this, 1 * 9, "Chicken bag");;
  }

  static boolean spawn (Location location, ItemStack[] itemStacks, CraftWorld world)
  {
    GoldRushChicken chicken = new GoldRushChicken (world.getHandle ());
    chicken.setPosition (location.getX(), location.getY(), location.getZ());
    Plugin.logger.info ("Spawning chicken at location: " + chicken.locX + ", " + chicken.locY + ", " + chicken.locZ);
    if (itemStacks != null)
    {
      chicken.fillBag (itemStacks);
    }
    if (!chicken.world.addEntity (chicken, CreatureSpawnEvent.SpawnReason.CUSTOM))
    {
      Plugin.logger.warning ("Spawning chicken failed, saving for later try...");
      delayedChicken.add (new Delayed (itemStacks, location));
      return false;
    }
    Plugin.addChicken (chicken.getUniqueID ());
    return true;
  }
  static void spawnDelayed (World world)
  {
    CraftWorld craftWorld = (CraftWorld) world;
    List<Delayed> not_spawned = new ArrayList<> (delayedChicken);
    delayedChicken.clear ();
    for (Delayed delayed : not_spawned)
    {
      delayed.spawn (craftWorld);
    }
  }

  void fillBag (ItemStack[] itemStacks)
  {
    bag.setStorageContents (itemStacks);
  }

  @Override
  public Inventory getInventory ()
  {
    return bag;
  }

  @Override
  protected void r ()
  {
    this.goalSelector.a (0, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 6.0F));
    // Eat grass
    this.goalSelector.a (1, new PathfinderGoalEatTile (this));
    // Ability to swim
    this.goalSelector.a(2, new PathfinderGoalFloat(this));
    // Randomly look around
    this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));

    //this.goalSelector.a (0, new PathfinderGoalDigForResource (this, new Location (bukkitWorld, locX, locY, locZ), 1.0));
    //this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntitySpider.class, 0, false));
  }

  private void setAttributes ()
  {
    this.setCustomName ("Goldie");
    this.setCustomNameVisible (true);
  }
}
