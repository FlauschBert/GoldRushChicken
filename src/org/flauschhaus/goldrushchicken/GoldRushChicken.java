package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.util.Set;

public class GoldRushChicken extends EntityChicken
{
  private World bukkitWorld;

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    forgetGoals ();
    addGoals ();
    setAttributes ();

    bukkitWorld = null;
  }

  public GoldRushChicken (World world)
  {
    // net.minecraft.server.v1_12_R1.World mcWorld = ((CraftWorld) world).getHandle ();
    super (((CraftWorld) world).getHandle ());
    forgetGoals ();
    addGoals ();
    setAttributes ();

    bukkitWorld = world;
  }

  private void setAttributes ()
  {
    this.setCustomName ("Goldie");
    this.setCustomNameVisible (true);
    this.canPickUpLoot = true;
  }

  private void addGoals ()
  {
    this.goalSelector.a (0, new PathFinderGoalWalk (this, new Location (bukkitWorld, locX, locY, locZ), 1.0));
    this.goalSelector.a(1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 6.0F));
  }

  private void forgetGoals ()
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
