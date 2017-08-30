package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.NavigationAbstract;
import net.minecraft.server.v1_12_R1.PathEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Location;


public class PathFinderGoalWalk extends PathfinderGoal
{
  private double speed;
  private EntityInsentient entity;
  private Location loc;
  private NavigationAbstract navigation;

  public PathFinderGoalWalk (EntityInsentient entity, Location loc, double speed)
  {
    this.entity = entity;
    this.loc = loc;
    this.navigation = this.entity.getNavigation();
    this.speed = speed;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    Main.logger.info ("a() called");
    return true;
  }

  // onStart () - called only once - init
  @Override
  public void c()
  {
    Main.logger.info ("c() called");
    PathEntity pathEntity = this.navigation.a(loc.getX(), loc.getY(), loc.getZ());
    this.navigation.a(pathEntity, speed);
  }

  @Override
  public void d()
  {
    Main.logger.info ("d() called");
  }

  // updateTask () - called repeatedly
  @Override
  public void e()
  {
    Main.logger.info ("e() called");
  }
}
