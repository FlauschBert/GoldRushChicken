package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.EntityAnimal;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;

public class Helper
{
  // Use EnumParticle.HEART for instance
  public static void showParticle (EntityAnimal animal, EnumParticle particle)
  {
    World world = animal.getWorld ();
    Random random = animal.getRandom ();
    for (int i = 0; i < 7; ++i)
    {
      double d0 = random.nextGaussian () * 0.02D;
      double d1 = random.nextGaussian () * 0.02D;
      double d2 = random.nextGaussian () * 0.02D;
      double d3 = random.nextDouble () * (double) animal.width * 2.0D - (double) animal.width;
      double d4 = 0.5D + random.nextDouble () * (double) animal.length;
      double d5 = random.nextDouble () * (double) animal.width * 2.0D - (double) animal.width;
      world.addParticle (particle, animal.locX + d3, animal.locY + d4, animal.locZ + d5, d0, d1, d2, new int[0]);
    }
  }

  static int getLightLevel (EntityLiving entityLiving)
  {
    Location pos = new Location (entityLiving.world.getWorld (), entityLiving.locX, entityLiving.locY - 1, entityLiving.locZ);
    return pos.getBlock ().getLightLevel ();
  }



}
