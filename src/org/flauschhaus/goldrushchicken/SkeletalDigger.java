package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class SkeletalDigger extends EntitySkeletonWither
{
  public SkeletalDigger (World world)
  {
    super(world);
  }

  @Override
  protected void r ()
  {
    // Ability to swim
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a (1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 8.0F));
    // Randomly look around
    this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
    // Attack creeper
    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityCreeper.class, true));
  }

  static boolean spawn (Location location, CraftWorld world)
  {
    SkeletalDigger skeletonDigger = new SkeletalDigger (world.getHandle ());
    skeletonDigger.setLocation (location.getX(), location.getY(), location.getZ(),
      location.getYaw(), location.getPitch());
    Plugin.logger.info ("Spawning skeletal digger at location: " + skeletonDigger.locX + ", " + skeletonDigger.locY + ", " + skeletonDigger.locZ);
    if (!world.getHandle().addEntity(skeletonDigger))
    {
      Plugin.logger.warning ("Spawning skeletal digger failed, saving for later try...");
      return false;
    }
    //Plugin.addChicken (chicken.getUniqueID ());
    return true;
  }
}
