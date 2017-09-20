package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.ArrayList;


public class PathfinderGoalDigForResource extends PathfinderGoal
{
  private final int distance = 60;

  private EntityInsentient entity;

  // Found block to move to
  private Block resourceDst = null;

  private ArrayList<Material> materials;
  private Target walkDst = null;

  // Initial false: no waiting; true: wait until tick
  private boolean waiting = false;
  private final int waitTicksForFallingBlock = 25;

  PathfinderGoalDigForResource (EntityInsentient entity, ArrayList<Material> materials, Target walkDst)
  {
    this.entity = entity;
    this.materials = materials;
    this.walkDst = walkDst;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    Plugin.logger.info ("a() called");

    // If nothing found yet call updateTask()
    if (resourceDst == null)
      return true;

    // If resource found and reached the block reset
    if (reachedResource ())
    {
      Plugin.logger.info ("Found resourceDst: " + resourceDst.getLocation ());

      // Reset to search for new resourceDst
      resourceDst = null;
      walkDst.block = null;
      waiting = false;
      // Search xz first before going down again
      walkDst.lastDigDown = true;
    }

    // Found but not reached the block call updateTask()
    return true;
  }

  // onStart () - called only once - init
  @Override
  public void c()
  {
    Plugin.logger.info ("c() called");
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

    if (waiting)
      return;

    // Wait for goto last dig goal
    if (walkDst.block != null)
      return;

    // If no resourceDst try to find goal inside distance blocks
    if (null == resourceDst)
    {
      //msg ("Found nothing yet. Try to find goal.");
      resourceDst = getGoalAvailable();

      if (null != resourceDst)
        Plugin.logger.info ("Set resourceDst: " + resourceDst.getLocation ());
    }

    digAndMove ();
  }

  private void digAndMove ()
  {
    int livX = (int) Math.floor (entity.locX);
    int livY = (int) Math.floor (entity.locY);
    int livZ = (int) Math.floor (entity.locZ);

    int digX = livX;
    int digY = livY;
    int digZ = livZ;

    // If no resourceDst found yet dig down
    if (resourceDst == null)
    {
      if (walkDst.lastDigDown)
      {
        digX += 1;
        walkDst.lastDigDown = false;
      }
      else
      {
        digY -= 1;
        walkDst.lastDigDown = true;
      }
    }
    else
    {
      int tarX = resourceDst.getX();
      int tarY = resourceDst.getY();
      int tarZ = resourceDst.getZ();

      int dX = getDigAmount (tarX - livX);
      int dY = getDigAmount (tarY - livY);
      int dZ = getDigAmount (tarZ - livZ);

      if (walkDst.lastDigDown &&
          (dX != 0 ||
           dZ != 0))
      {
        if (dX != 0)
        {
          digX += dX;
        }
        else // if (dZ != 0)
        {
          digZ += dZ;
        }

        walkDst.lastDigDown = false;
      }
      else if (dY != 0)
      {
        digY += dY;
        walkDst.lastDigDown = true;
      }
      else
      {
        walkDst.lastDigDown = true;
      }
    }

    // Clear second block above
    breakBlockAndSetWaiting (digX, digY + 1, digZ);

    // Check block above two blocks
    {
      Block blockToAir = getWorld().getBlockAt(digX, digY + 2, digZ);

      // Check for falling blocks and wait if necessary
      if (isBlockFalling (blockToAir))
        wait (waitTicksForFallingBlock);
    }

    walkDst.block = breakBlockAndSetWaiting (digX, digY, digZ);
  }

  private Block getGoalAvailable()
  {
    int currentX = (int) Math.floor (entity.locX);
    int currentY = (int) Math.floor (entity.locY);
    int currentZ = (int) Math.floor (entity.locZ);

    for (int d = 1; d < distance / 2; ++d)
    {

      int[] xPos = new int [2];
      xPos [0] = currentX + d;
      xPos [1] = currentX - d;
      {
        for (int x : xPos) {
          for (int y = currentY - d; y <= currentY + d; ++y) {
            for (int z = currentZ - d; z <= currentZ + d; ++z) {
              Block block = getWorld().getBlockAt(x, y, z);
              if (goalFound (block)) {
                return block;
              }
            }
          }
        }
      }

      int[] zPos = new int [2];
      zPos [0] = currentZ + d;
      zPos [1] = currentZ - d;
      {
        for (int z : zPos) {
          for (int y = currentY - d; y <= currentY + d; ++y) {
            for (int x = currentX - d; x <= currentX + d; ++x) {
              Block block = getWorld().getBlockAt(x, y, z);
              if (goalFound (block)) {
                return block;
              }
            }
          }
        }
      }

      int[] yPos = new int [2];
      yPos [0] = currentY + d;
      yPos [1] = currentY - d;
      {
        for (int y : yPos) {
          for (int z = currentZ - d; z <= currentZ + d; ++z) {
            for (int x = currentX - d; x <= currentX + d; ++x) {
              Block block = getWorld().getBlockAt(x, y, z);
              if (goalFound (block)) {
                return block;
              }
            }
          }
        }
      }
    }

    return null;
  }

  private Block breakBlockAndSetWaiting (int x, int y, int z)
  {
    Block blockToBreak = getWorld().getBlockAt(x, y, z);

    // Check for falling blocks and wait if necessary
    if (isBlockFalling (blockToBreak))
      wait (waitTicksForFallingBlock);

    // FIXME: Check for block found by accident
    //if (goalFound(blockToBreak))
    //{
      // Drop block as item(s)
    //  blockToBreak.dropBlockAsItem(false);
    //}

    // FIXME: drops?
    blockToBreak.breakNaturally ();
    return blockToBreak;
  }

  private int getDigAmount (int diff)
  {
    if (diff > 0)
    {
      return 1;
    }
    else if (diff < 0)
    {
      return -1;
    }
    return 0;
  }

  private boolean isBlockFalling (Block block)
  {
    return
      block.getType () == Material.SAND ||
      block.getType () == Material.GRAVEL;
  }

  private boolean goalFound (Block block)
  {
    return materials.contains(block.getType());
  }

  private org.bukkit.World getWorld ()
  {
    return entity.world.getWorld ();
  }

  private void wait (int ticks)
  {
    waiting = true;
    Bukkit.getScheduler ().scheduleSyncDelayedTask (Plugin.plugin, new Runnable ()
    {
      @Override
      public void run ()
      {
        waiting = false;
      }
    }, ticks);
  }

  private boolean reachedResource ()
  {
    return
      (int) Math.floor (entity.locX) == resourceDst.getX () &&
      (int) Math.floor (entity.locY) == resourceDst.getY () &&
      (int) Math.floor (entity.locZ) == resourceDst.getZ ();

  }
}
