package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityChicken;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.util.Set;

public class GoldRushChicken extends EntityChicken
{
  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);
    forgetGoals ();
  }

  public GoldRushChicken (World world)
  {
    // net.minecraft.server.v1_12_R1.World mcWorld = ((CraftWorld) world).getHandle ();
    super (((CraftWorld) world).getHandle ());
    forgetGoals ();
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
