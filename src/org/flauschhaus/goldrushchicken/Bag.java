package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bag
{
  private static final String tagName = "info";
  private static final String listTagName = "org.flauschhaus.infos";
  private Inventory inventory;
  private UUID uuid;
  private ItemStack feather = new ItemStack (Material.FEATHER, 1);

  public Bag (InventoryHolder holder, UUID uuid)
  {
    this.uuid = uuid;
    inventory = Bukkit.createInventory (holder, 1 * 9, "Chicken bag");
    readItemStacks ();
  }

  public Inventory getInventory ()
  {
    return inventory;
  }

  public void saveItemStacks ()
  {
    List<String> infos = new ArrayList<String> ();
    infos.add (uuid.toString ());
    feather = setItemStacksTo (feather, infos);
    Plugin.logger.info ("Wrote: " + uuid.toString ());
  }

  public void readItemStacks ()
  {
    List<String> infos = getItemStacksFrom (feather);
    if (!infos.isEmpty ())
    {
      Plugin.logger.info ("Read: " + infos.get (0));
    }
  }

  private static NBTTagList convert (List<String> infos)
  {
    NBTTagList tagList = new NBTTagList ();
    for (String info: infos)
    {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString (tagName, info);
      tagList.add (tag);
    }
    return tagList;
  }

  // Add info for saving to item stack
  private static ItemStack setItemStacksTo (ItemStack item, List<String> infos)
  {
    // Setup tag for saving
    NBTTagCompound compound = new NBTTagCompound ();
    compound.set (listTagName, convert (infos));
    // Convert to NMS item stack and set tag
    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy (item);
    nmsItem.setTag (compound);
    // Convert back to bukkit item stack
    return CraftItemStack.asBukkitCopy (nmsItem);
  }

  // Get saved info from item stack
  private static List<String> getItemStacksFrom (ItemStack item)
  {
    List<String> itemStacks = new ArrayList<String> ();
    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy (item);
    NBTTagCompound compound = nmsItem.getTag ();
    try
    {
      NBTTagList tagList = (NBTTagList) compound.get (listTagName);
      for (int i = 0; i < tagList.size(); ++i)
      {
        NBTTagCompound tag = tagList.get (i);
        itemStacks.add (tag.getString (tagName));
      }
    }
    catch (NullPointerException e)
    {
      Plugin.logger.info ("No compound tag " + listTagName + " found");
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
    return itemStacks;
  }
}
