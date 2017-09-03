package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Set;

public class GoldRushChicken extends EntityChicken implements InventoryHolder
{
  private Bag bag;

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    forgetVanillaGoals ();
    addOwnGoals ();
    setAttributes ();

    bag = new Bag (this, getUniqueID ());

    // Test save read
    bag.saveItemStacks ();
    bag.readItemStacks ();
  }

  @Override
  public Inventory getInventory ()
  {
    return bag.getInventory ();
  }

  public void saveInventory ()
  {
    bag.saveItemStacks ();
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
