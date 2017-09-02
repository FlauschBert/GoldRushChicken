package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper
{
  public static Object getPrivateMember (String fieldName, Class clazz, Object object)
  {
    try
    {
      Field field = clazz.getDeclaredField (fieldName);
      field.setAccessible (true);
      return field.get (object);
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
    return null;
  }

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

  private static NBTTagList convert (List<String> infos)
  {
    NBTTagList tagList = new NBTTagList ();
    for (String info: infos)
    {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString ("info", info);
      tagList.add (tag);
    }
    return tagList;
  }

  // Add info for saving to item stack
  public static ItemStack setItemStacksTo (ItemStack item, List<String> infos)
  {
    // Setup tag for saving
    NBTTagCompound compound = new NBTTagCompound ();
    compound.set ("org.flauschhaus.infos", convert (infos));
    // Convert to NMS item stack and set tag
    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy (item);
    nmsItem.setTag (compound);
    // Convert back to bukkit item stack
    return CraftItemStack.asBukkitCopy (nmsItem);
  }

  // Get saved info from item stack
  public static List<String> getItemStacksFrom (ItemStack item)
  {
    List<String> itemStacks = new ArrayList<String> ();
    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy (item);
    NBTTagCompound compound = nmsItem.getTag ();
    NBTTagList tagList = (NBTTagList) compound.get ("org.flauschhaus.infos");
    for (int i = 0; i < tagList.size(); ++i)
    {
      NBTTagCompound tag = tagList.get (i);
      itemStacks.add (tag.getString ("info"));
    }
    return itemStacks;
  }
}
