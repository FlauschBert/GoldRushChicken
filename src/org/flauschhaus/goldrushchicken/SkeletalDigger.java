package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SkeletalDigger extends EntitySkeletonWither
{
  public SkeletalDigger (World world)
  {
    super(world);
    this.setCustomName ("Digga");
    this.setCustomNameVisible (true);
  }

  @Override
  public GroupDataEntity prepare(DifficultyDamageScaler dds, GroupDataEntity gde) {
    // Calling the super method FIRST, so in case it changes the equipment, our equipment overrides it.
    gde = super.prepare(dds, gde);
    // We'll set the main hand to a bow and head to a pumpkin now!
    this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.DIAMOND_PICKAXE));
    this.setSlot (EnumItemSlot.HEAD, new ItemStack (Blocks.PUMPKIN));

    // Last, returning the GroupDataEntity called gde.
    return gde;
  }

  @Override
  protected void r ()
  {
    // Ability to swim
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a (1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 8.0F));
    // Randomly look around
    this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
    this.goalSelector.a (3, new PathfinderGoalDigForGold (this));
    // Attack creeper
    this.targetSelector.a(4, new PathfinderGoalNearestAttackableTarget<>(this, EntityCreeper.class, true));
  }

  static boolean spawn (Location location, CraftWorld world)
  {
    SkeletalDigger skeletalDigger = new SkeletalDigger (world.getHandle ());
    skeletalDigger.setPosition (location.getX(), location.getY(), location.getZ());
    if (!skeletalDigger.world.addEntity (skeletalDigger, CreatureSpawnEvent.SpawnReason.CUSTOM))
    {
      Plugin.logger.info ("Spawning skeletal digger failed");
      return false;
    }
    Plugin.logger.info ("Spawning skeletal digger at location: " + skeletalDigger.locX + ", " + skeletalDigger.locY + ", " + skeletalDigger.locZ);

    Plugin.addDigger (skeletalDigger.getUniqueID ());

    // Add/Update pickaxe and pumpkin
    ((EntityInsentient) skeletalDigger).prepare(skeletalDigger.world.D(new BlockPosition(skeletalDigger)), (GroupDataEntity) null);

//    Bukkit.getScheduler ().scheduleSyncRepeatingTask (Plugin.plugin, new Runnable ()
//    {
//      int value = 5;
//
//      @Override
//      public void run ()
//      {
//        Player player = Bukkit.getServer ().getPlayer ("FlauschBert");
//        if (player != null)
//        {
//          player.sendRawMessage ("Value: " + value);
//        }
//
//        // hit stuff: 20 (spreading stars), 35 (fireworks)
//        skeletalDigger.world.broadcastEntityEffect (skeletalDigger, (byte) value);
//        ++value;
//      }
//    }, 0, 40);

    return true;
  }
}
