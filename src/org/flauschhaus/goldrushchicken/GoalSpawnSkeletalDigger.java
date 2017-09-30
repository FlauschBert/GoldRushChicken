package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/*! Spawns a skeletal digger if the gold rush chicken has
    seeds in the inventory
    and no skeletal digger is active
 */
public class GoalSpawnSkeletalDigger extends PathfinderGoal
{
  private GoldRushChicken chicken;
  private Material spawnReason = Material.SEEDS;

  GoalSpawnSkeletalDigger (GoldRushChicken entity)
  {
    this.chicken = entity;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    //Plugin.logger.info ("GoalSpawnSkeletalDigger a() called");

    if (chicken.digga == null)
      return getItemStack (spawnReason) != null;

    return false;
  }

  // onStart () - called only once - init
  @Override
  public void c()
  {
    Plugin.logger.info ("GoalSpawnSkeletalDigger c() called");
    chicken.getInventory ().remove (new ItemStack (spawnReason, 1));
  }

  // onFinish () - called only once - after returns false the first time
  @Override
  public void d()
  {
    Plugin.logger.info ("GoalSpawnSkeletalDigger d() called");
  }

  // updateTask () - called repeatedly if a() returns true
  @Override
  public void e()
  {
    Plugin.logger.info ("GoalSpawnSkeletalDigger e() called");

    Location location = new Location (chicken.world.getWorld (), chicken.locX, chicken.locY, chicken.locZ);
    location = addRandomDirection (location);
    chicken.digga = SkeletalDigger.spawn (location, chicken.world.getWorld ());
    chicken.follow (chicken.digga);
  }

  private Location addRandomDirection (Location location)
  {
    Random random = chicken.getRandom ();
    int direction = random.nextInt (4);
    if (direction == 0)
      location.add (1.0, 0.0, 0.0);
    else if (direction == 1)
      location.add (-1.0, 0.0, 0.0);
    else if (direction == 2)
      location.add (0.0, 0.0, 1.0);
    else if (direction == 3)
      location.add (0.0, 0.0, -1.0);
    return location;
  }

  private ItemStack getItemStack (Material material)
  {
    Inventory inventory = chicken.getInventory ();
    if (inventory == null)
      return null;

    ItemStack[] itemStacks = inventory.getStorageContents ();
    for (ItemStack stack : itemStacks)
      if (stack != null &&
          stack.getType () == material)
        return stack;
    return null;
  }
}
