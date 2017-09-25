package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;

public class PathfinderGoalGotoLastDig extends PathfinderGoal
{
  private EntityInsentient entity;
  private double speed;
  private Target walkDst = null;

  private int x, y, z;

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

    if (walkDst.waiting)
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

    if (reachedGoal ())
    {
      walkDst.block = null;
      return;
    }

    // Except the first two times (always false) it returns whether the path is free
    entity.getNavigation ().a (x, y, z, speed);

    Plugin.logger.info ("Walking at: " + entity.locX + ", " + entity.locY + ", " + entity.locZ);
  }

  private boolean reachedGoal ()
  {
    boolean success =
      (int) Math.floor (entity.locX) == x &&
      (int) Math.floor (entity.locY) == y &&
      (int) Math.floor (entity.locZ) == z;

    if (success)
      Plugin.logger.info ("Goal reached: " + x + ", " + y + ", " + z);

    return success;
  }

  private void updateGoal ()
  {
    x = walkDst.block.getX ();
    y = walkDst.block.getY ();
    z = walkDst.block.getZ ();

    // Problem here is that the entity can fall below the
    // walking destination so it never can reach it
    //
    // So we have to correct this here
    if (y > (int) Math.floor (entity.locY))
      y = (int) Math.floor (entity.locY);

    Plugin.logger.info ("Setting goal to: " + x + ", " + y + ", " + z);
  }
}
