package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.Material;
import java.util.ArrayList;
import java.util.Random;


public class PathfinderGoalDigForResource extends PathfinderGoal
{
  private final int distance = 60;

  private EntityInsentient entity;

  private Random random;
  private final int minSameDirection = 5;
  private int sameDirectionCount = 0;
  private int lastDirection = 0;

  // Found block to move to
  private Block resourceDst = null;

  private ArrayList<Material> materials;
  private Target walkDst = null;

  private int lightCounter = 0;
  private final int lightThreshold = 8;

  // Initial false: no waiting; true: wait until tick
  // walkDst.waiting
  private final int waitTicksForFallingBlock = 25;

  PathfinderGoalDigForResource (EntityInsentient entity, ArrayList<Material> materials, Target walkDst)
  {
    this.entity = entity;
    this.random = entity.getRandom ();
    this.materials = materials;
    this.walkDst = walkDst;
  }

  // shouldStart () - are we still active?
  @Override
  public boolean a()
  {
    Plugin.logger.info ("PathfinderGoalDigForResource a() called");

    if (walkDst.waiting)
      return false;

    // Wait for goto last dig goal
    if (walkDst.block != null)
      return false;

    // If resource removed go on searching
    if (diggedResource () ||
        tooFarBelowResource ())
    {
      logBlock ("Resetting resourceDst", resourceDst.getX (), resourceDst.getY (), resourceDst.getZ (), resourceDst);

      // Reset to search for new resourceDst
      resourceDst = null;

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
    Plugin.logger.info ("PathfinderGoalDigForResource c() called");
  }

  // onFinish () - called only once - after returns false the first time
  @Override
  public void d()
  {
    Plugin.logger.info ("PathfinderGoalDigForResource d() called");
  }

  // updateTask () - called repeatedly if a returns true
  @Override
  public void e()
  {
    Plugin.logger.info ("PathfinderGoalDigForResource e() called");

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
      Plugin.logger.info ("Dig and move ...");
      if (walkDst.lastDigDown)
      {
        int direction = lastDirection;
        ++sameDirectionCount;
        if (sameDirectionCount > minSameDirection)
        {
          // random number between 0-1
          direction = random.nextInt (2);
          lastDirection = direction;
          sameDirectionCount = 0;
        }

        if (direction == 0)
          digX += 1;
        else
          digZ += 1;

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
      Plugin.logger.info ("Resource ...");

      int tarX = resourceDst.getX();
      int tarY = resourceDst.getY();
      int tarZ = resourceDst.getZ();

      logBlock ("Locked resource", tarX, tarY, tarZ, resourceDst);

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

    int waitTicks = 0;
    waitTicks += breakBlockAndAddWait (digX, digY + 3, digZ, false);
    waitTicks += breakBlockAndAddWait (digX, digY + 2, digZ, true);
    waitTicks += breakBlockAndAddWait (digX, digY + 1, digZ, true);
    waitTicks += breakBlockAndAddWait (digX, digY, digZ, true);

    // Add light to the non broken block above (if not air)
    addLight (digX, digY + 3, digZ);

    // Wait if necessary
    if (waitTicks > 0)
    {
      Plugin.logger.info ("Waiting: " + waitTicks + " ticks");
      wait (waitTicks);
    }

    // Set new destination to walk to
    walkDst.block = getWorld ().getBlockAt (digX, digY, digZ);
  }

  private void addLight (int x, int y, int z)
  {
    ++lightCounter;
    if (lightCounter < lightThreshold)
      return;
    lightCounter = 0;

    Block lightBlock = getWorld ().getBlockAt (x, y, z);
    if (lightBlock.getType () != Material.AIR &&
      !resourceFound (lightBlock))
      lightBlock.setType (Material.GLOWSTONE);
  }

  private Block getGoalAvailable()
  {
    int currentX = (int) Math.floor (entity.locX);
    int currentY = (int) Math.floor (entity.locY);
    int currentZ = (int) Math.floor (entity.locZ);

    for (int d = 1; d < distance / 2; ++d)
    {
      Block blockX = getBlockX (currentX, currentY, currentZ, d);
      if (blockX != null)
        return blockX;

      Block blockZ = getBlockZ (currentX, currentY, currentZ, d);
      if (blockZ != null)
        return blockZ;

      Block blockY = getBlockY (currentX, currentY, currentZ, d);
      if (blockY != null)
        return blockY;
    }

    return null;
  }

  private Block getBlockZ (int currentX, int currentY, int currentZ, int d)
  {
    int[] yPos = new int [2];
    yPos [0] = currentY + d;
    yPos [1] = currentY - d;
    {
      for (int y : yPos) {
        for (int z = currentZ - d; z <= currentZ + d; ++z) {
          for (int x = currentX - d; x <= currentX + d; ++x) {
            Block block = getWorld().getBlockAt(x, y, z);
            if (resourceFound (block)) {
              return block;
            }
          }
        }
      }
    }
    return null;
  }

  private Block getBlockY (int currentX, int currentY, int currentZ, int d)
  {
    int[] zPos = new int [2];
    zPos [0] = currentZ + d;
    zPos [1] = currentZ - d;
    {
      for (int z : zPos) {
        for (int y = currentY - d; y <= currentY + d; ++y) {
          for (int x = currentX - d; x <= currentX + d; ++x) {
            Block block = getWorld().getBlockAt(x, y, z);
            if (resourceFound (block)) {
              return block;
            }
          }
        }
      }
    }
    return null;
  }

  private Block getBlockX (int currentX, int currentY, int currentZ, int d)
  {
    int[] xPos = new int [2];
    xPos [0] = currentX + d;
    xPos [1] = currentX - d;
    {
      for (int x : xPos) {
        for (int y = currentY - d; y <= currentY + d; ++y) {
          for (int z = currentZ - d; z <= currentZ + d; ++z) {
            Block block = getWorld().getBlockAt(x, y, z);
            if (resourceFound (block)) {
              return block;
            }
          }
        }
      }
    }
    return null;
  }

  private int breakBlockAndAddWait (int x, int y, int z, boolean breakBlock)
  {
    int waitTicks = 0;

    Block blockToBreak = getWorld ().getBlockAt (x, y, z);
    logBlock ("breakBlockAndAddWait", x, y, z, blockToBreak);

    // Check for falling blocks and wait if necessary
    if (blockWillFall (blockToBreak))
      waitTicks += waitTicksForFallingBlock;

    if (breakBlock)
      blockToBreak.breakNaturally ();

    return waitTicks;
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

  private boolean blockWillFall (Block block)
  {
    return
      block.getType () == Material.SAND ||
      block.getType () == Material.GRAVEL;
  }

  private boolean resourceFound (Block block)
  {
    return materials.contains(block.getType());
  }

  private org.bukkit.World getWorld ()
  {
    return entity.world.getWorld ();
  }

  private void wait (int ticks)
  {
    walkDst.waiting = true;
    Bukkit.getScheduler ().scheduleSyncDelayedTask (Plugin.plugin, new Runnable ()
    {
      @Override
      public void run ()
      {
        walkDst.waiting = false;
      }
    }, ticks);
  }

  private boolean tooFarBelowResource ()
  {
    int livY = (int) Math.floor (entity.locY);
    return
      resourceDst != null &&
      resourceDst.getY () > livY + 2;
  }

  private boolean diggedResource ()
  {
    return
      resourceDst != null &&
      !resourceFound (resourceDst);
  }

  private void logBlock (String info, int x, int y, int z, Block block)
  {
    Plugin.logger.info (info + ": " + x + ", " + y + ", " + z + "; " + block.getType ().toString ());
  }
}
