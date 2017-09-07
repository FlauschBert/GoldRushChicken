package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import javax.annotation.Nullable;
import java.util.Calendar;

public class SkeletalDigger extends EntitySkeletonWither
{
  public SkeletalDigger (World world)
  {
    super(world);
  }

  @Override
  protected void a(DifficultyDamageScaler difficultydamagescaler) {
    this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
  }

  @Override
  public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity) {
    groupdataentity = super.prepare(difficultydamagescaler, groupdataentity);
    this.a(difficultydamagescaler);
    this.b(difficultydamagescaler);
    this.dm();
    this.m(this.random.nextFloat() < 0.55F * difficultydamagescaler.d());
    this.setSlot(EnumItemSlot.HEAD, new ItemStack(Blocks.LIT_PUMPKIN));
    this.dropChanceArmor[EnumItemSlot.HEAD.b()] = 0.0F;

    return groupdataentity;
  }

  @Override
  protected void r ()
  {
    this.goalSelector.a (1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 6.0F));
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
