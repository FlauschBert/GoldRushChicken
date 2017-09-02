package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public class GoldRushChicken extends EntityChicken implements InventoryHolder
{
  private World bukkitWorld;
  private Inventory inventory;

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    forgetVanillaGoals ();
    addOwnGoals ();
    setAttributes ();
    setupInventory ();

    bukkitWorld = null;
  }

  public GoldRushChicken (World world)
  {
    // net.minecraft.server.v1_12_R1.World mcWorld = ((CraftWorld) world).getHandle ();
    super (((CraftWorld) world).getHandle ());
    forgetVanillaGoals ();
    addOwnGoals ();
    setAttributes ();
    setupInventory ();

    bukkitWorld = world;
  }

  private void setAttributes ()
  {
    this.setCustomName ("Goldie");
    this.setCustomNameVisible (true);
    this.canPickUpLoot = true;
  }

  public void openInventory (CraftPlayer player)
  {
    player.openInventory (getInventory ());
  }

  private void setupInventory ()
  {
    inventory = Bukkit.createInventory (this, 1 * 9, "Chicken bag");
  }

  @Override
  public Inventory getInventory ()
  {
    return inventory;
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
