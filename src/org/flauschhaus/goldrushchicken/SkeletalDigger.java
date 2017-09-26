package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;

class Target
{
  Block block = null;
  boolean lastDigDown = false;
  boolean waiting = false;
}

public class SkeletalDigger extends EntitySkeletonWither
{
  private Target lastDigPosition = null;

  SkeletalDigger (World world)
  {
    super(world);
    this.setCustomName ("Digga");
    this.setCustomNameVisible (true);
  }

  private ArrayList<Material> getMaterials ()
  {
    ArrayList<Material> materials = new ArrayList<Material> ();
    materials.add (Material.GOLD_ORE);
    materials.add (Material.DIAMOND_ORE);
    materials.add (Material.IRON_ORE);
    materials.add (Material.LAPIS_ORE);
    materials.add (Material.COAL_ORE);
    return materials;
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
    if (lastDigPosition == null)
      lastDigPosition = new Target ();

    // Ability to swim
    this.goalSelector.a(0, new PathfinderGoalFloat(this));
    this.goalSelector.a (1, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 8.0F));
    // Randomly look around
    this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
    this.goalSelector.a (3, new PathfinderGoalDigForResource (this, getMaterials (), lastDigPosition));
    this.goalSelector.a (4, new PathfinderGoalGotoLastDig (this, lastDigPosition, 2.0D));
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
