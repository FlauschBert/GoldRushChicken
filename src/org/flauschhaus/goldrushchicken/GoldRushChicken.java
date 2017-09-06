package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityChicken;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GoldRushChicken extends EntityChicken implements InventoryHolder
{
  private Inventory bag;
  private static List<Delayed> delayedChicken = new ArrayList<> ();

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    forgetVanillaGoals ();
    addOwnGoals ();
    setAttributes ();

    bag = Bukkit.createInventory (this, 1 * 9, "Chicken bag");;
  }

  static boolean spawn (Location location, ItemStack[] itemStacks, CraftWorld world)
  {
    GoldRushChicken chicken = new GoldRushChicken (world.getHandle ());
    chicken.setLocation (location.getX(), location.getY(), location.getZ(),
                         location.getYaw(), location.getPitch());
    Plugin.logger.info ("Spawning chicken at location: " + chicken.locX + ", " + chicken.locY + ", " + chicken.locZ);
    if (itemStacks != null)
    {
      chicken.fillBag (itemStacks);
    }
    if (!world.getHandle().addEntity(chicken))
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

  private void setAttributes ()
  {
    this.setCustomName ("Goldie");
    this.setCustomNameVisible (true);
  }

  private void addOwnGoals ()
  {
    //this.goalSelector.a (0, new PathFinderGoalWalk (this, new Location (bukkitWorld, locX, locY, locZ), 1.0));
    this.goalSelector.a(1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 6.0F));
    //this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntitySpider.class, 0, false));
  }

  private void forgetVanillaGoals ()
  {
    Set setGB = (Set) Helper.getPrivateMember ("b", PathfinderGoalSelector.class, this.goalSelector);
    setGB.clear ();
    Set setGC = (Set) Helper.getPrivateMember ("c", PathfinderGoalSelector.class, this.goalSelector);
    setGC.clear ();
    Set setTB = (Set) Helper.getPrivateMember ("b", PathfinderGoalSelector.class, this.targetSelector);
    setTB.clear ();
    Set setTC = (Set) Helper.getPrivateMember ("c", PathfinderGoalSelector.class, this.targetSelector);
    setTC.clear ();
  }
}
