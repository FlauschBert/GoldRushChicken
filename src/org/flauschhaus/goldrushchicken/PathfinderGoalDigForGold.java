package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;


public class PathfinderGoalDigForGold extends PathfinderGoal
{
  private EntityInsentient entity;
  private double speed = 0.5d;
  private double x, y, z;


  public PathfinderGoalDigForGold (EntityInsentient entity)
  {
    this.entity = entity;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    Plugin.logger.info ("a() called");

    if (entity.aI () >= 100)
    {
      return false;
    }

    return true;
  }

  // onStart () - called only once - init
  @Override
  public void c()
  {
    Plugin.logger.info ("c() called");
    updateGoal ();
  }

  // onFinish () - called only once - after returns false the first time
  @Override
  public void d()
  {
    Plugin.logger.info ("d() called");
  }

  // updateTask () - called repeatedly if a returns true
  @Override
  public void e()
  {
    Plugin.logger.info ("e() called");

    // Define new goal
    if (Math.floor (entity.locX) == Math.floor (x))
    {
      updateGoal ();
    }

    // Except the first two times (always false) it returns whether the path is free
    boolean value = entity.getNavigation ().a (x, y, z, speed);
    Plugin.logger.info ("Status: " + value);
  }

  private void updateGoal ()
  {
    x = entity.locX;
    y = entity.locY;
    z = entity.locZ;
    x += 1.5d;
    Plugin.logger.info ("Setting destination to " + x + ", " + y + ", " + z);
  }
}
