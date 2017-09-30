package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GoldRushChicken extends EntityChicken implements InventoryHolder
{
  private Inventory bag = null;
  private static List<Delayed> delayedChicken = new ArrayList<> ();
  SkeletalDigger digga = null;
  private int followTaskId = -1;

  public GoldRushChicken (net.minecraft.server.v1_12_R1.World world)
  {
    super(world);

    setCustomName ("Goldie");
    setCustomNameVisible (true);

    // Don't get despawned if far away from player
    ((LivingEntity) getBukkitEntity ()).setRemoveWhenFarAway (false);
    ((LivingEntity) getBukkitEntity ()).setCanPickupItems (true);

    bag = Bukkit.createInventory (this, 1 * 9, "Chicken bag");;
  }

  void follow (EntityInsentient entity)
  {
    followTaskId =
      Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.plugin, new Runnable()
      {
        @Override
        public void run()
        {
          Block block = world.getWorld ().getBlockAt (
            (int) entity.locX, (int) entity.locY, (int) entity.locZ);
          if (block.getType () == org.bukkit.Material.LAVA)
            return;

          double walkSpeed = 2.0D;
          getNavigation().a(entity.locX, entity.locY, entity.locZ, walkSpeed);
        }
      }, 0 * 10, 2 * 15);
  }

  static boolean spawn (Location location, ItemStack[] itemStacks, CraftWorld world)
  {
    GoldRushChicken chicken = new GoldRushChicken (world.getHandle ());
    chicken.setPosition (location.getX(), location.getY(), location.getZ());
    Plugin.logger.info ("Spawning chicken at location: " + chicken.locX + ", " + chicken.locY + ", " + chicken.locZ);
    if (itemStacks != null)
    {
      chicken.fillBag (itemStacks);
    }
    if (!chicken.world.addEntity (chicken, CreatureSpawnEvent.SpawnReason.CUSTOM))
    {
      Plugin.logger.warning ("Spawning chicken failed, saving for later try...");
      delayedChicken.add (new Delayed (itemStacks, location));
      return false;
    }
    Plugin.addChicken (chicken.getUniqueID ());

    // hit stuff: 18 (17? hearts), 20 (spreading stars), 35 (fireworks)
    chicken.world.broadcastEntityEffect (chicken, (byte) 20);
    return true;
  }
  static void spawnDelayed (World world)
  {
    CraftWorld craftWorld = (CraftWorld) world;
    List<Delayed> not_spawned = new ArrayList<> (delayedChicken);
    delayedChicken.clear ();
    for (Delayed delayed : not_spawned)
    {
      delayed.spawn (craftWorld);
    }
  }

  void fillBag (ItemStack[] itemStacks)
  {
    bag.setStorageContents (itemStacks);
  }

  @Override
  public Inventory getInventory ()
  {
    return bag;
  }

  @Override
  protected void r ()
  {
    this.goalSelector.a (0, new PathfinderGoalLookAtPlayer (this, EntityHuman.class, 6.0F));
    // Eat grass
    this.goalSelector.a (1, new PathfinderGoalEatTile (this));
    // Ability to swim
    this.goalSelector.a(2, new PathfinderGoalFloat(this));
    // Randomly look around
    this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
    this.goalSelector.a(4, new GoalSpawnSkeletalDigger (this));
  }
}
