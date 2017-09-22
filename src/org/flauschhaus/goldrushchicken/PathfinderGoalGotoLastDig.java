package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;

public class PathfinderGoalGotoLastDig extends PathfinderGoal
{
  private EntityInsentient entity;
  private double speed;
  private Target walkDst = null;

  private double x, y, z;

  PathfinderGoalGotoLastDig (EntityInsentient entity, Target walkDst, double speed)
  {
    this.entity = entity;
    this.walkDst = walkDst;
    this.speed = speed;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    Plugin.logger.info ("PathfinderGoalGotoLastDig a() called");

    // FIXME: ???
    if (entity.aI () >= 100)
      return false;

    return (walkDst.block != null);
  }

  // onStart () - called only once - init
  @Override
  public void c()
  {
    Plugin.logger.info ("PathfinderGoalGotoLastDig c() called");
    updateGoal ();
  }

  // onFinish () - called only once - after returns false the first time
  @Override
  public void d()
  {
    Plugin.logger.info ("PathfinderGoalGotoLastDig d() called");
  }

  // updateTask () - called repeatedly if a returns true
  @Override
  public void e()
  {
    Plugin.logger.info ("PathfinderGoalGotoLastDig e() called");

    // Except the first two times (always false) it returns whether the path is free
    if (!entity.getNavigation ().a (x, y, z, speed))
      return;

    if (reachedGoal ())
      walkDst.block = null;
  }

  private boolean reachedGoal ()
  {
    boolean success =
      Math.floor (entity.locX) == Math.floor (x) &&
      Math.floor (entity.locY) == Math.floor (y) &&
      Math.floor (entity.locZ) == Math.floor (z);

    if (! success &&
        Math.floor (entity.locY) < Math.floor (y))
    {
      success = true;
      walkDst.lastDigDown = true;
    }

    if (success)
      Plugin.logger.info ("Goal reached: " + x + ", " + y + ", " + z);

    return success;
  }

  private void updateGoal ()
  {
    x = walkDst.block.getX () + 0.5d;
    y = walkDst.block.getY () + 0.5d;
    z = walkDst.block.getZ () + 0.5d;
    Plugin.logger.info ("Setting goal to: " + x + ", " + y + ", " + z);
  }
}
