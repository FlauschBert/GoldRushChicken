package org.flauschhaus.goldrushchicken;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Storage
{
  private static final String tag_world = "world";
  private static final String tag_inv = "inv";
  private static final String tag_loc = "loc";

  private static int counter = 0;

  private static List<String> stringify (ItemStack[] itemStacks)
  {
    List<String> result = new ArrayList<> ();
    for (ItemStack itemStack : itemStacks)
    {
      try
      {
        result.add (itemStack.getType ().name ());
        result.add (Integer.toString (itemStack.getAmount ()));
      }
      catch (NullPointerException e)
      {
        // Empty
      }
    }
    return result;
  }

  private static ItemStack[] toItemStack (List<String> list)
  {
    if (list.size () % 2 != 0)
    {
      Plugin.logger.warning ("Bad item stack deserialization!");
      return null;
    }
    List<ItemStack> itemStacks = new ArrayList<> ();
    for (Iterator<String> it = list.iterator(); it.hasNext(); )
    {
      Material material = Material.getMaterial (it.next ());
      int amount = Integer.parseInt (it.next ());
      ItemStack itemStack = new ItemStack (material, amount);
      itemStacks.add (itemStack);
    }
    ItemStack[] itemStacksArray = new ItemStack[itemStacks.size()];
    return itemStacks.toArray(itemStacksArray);
  }

  private static List<Double> stringify (Location location)
  {
    List<Double> result = new ArrayList<Double> ();
    result.add (location.getX ());
    result.add (location.getY ());
    result.add (location.getZ ());
    result.add ((double)location.getPitch ());
    result.add ((double)location.getYaw ());
    return result;
  }

  private static Location toLocation (List<Double> list, World world)
  {
    if (list.size () != 5)
    {
      Plugin.logger.warning ("Bad location deserialization!");
      return null;
    }
    return new Location (
      world,
      list.get (0),
      list.get (0),
      list.get (0),
      list.get (0).floatValue (),
      list.get (0).floatValue ()
      );
  }

  static void init (Plugin plugin)
  {
    try
    {
      // Create data folder inside plugin data section
      // if not available
      if (!plugin.getDataFolder ().exists ())
      {
        plugin.getDataFolder ().mkdirs ();
      }

      // Create "config.yml" if not available
      plugin.saveDefaultConfig ();
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
  }

  enum Mode { READ, WRITE };
  static void reset (Mode mode, Plugin plugin)
  {
    if (mode == Mode.WRITE)
    {
      // Clear config file
      plugin.saveResource ("config.yml", true);
    }

    counter = 0;
  }

  static void add (Location location, ItemStack[] itemStacks, World world, Plugin plugin)
  {
    FileConfiguration config = plugin.getConfig ();
    config.set (tag_loc + counter, stringify (location));
    config.set (tag_inv + counter, stringify (itemStacks));
    config.set (tag_world + counter, world.getName ());
    counter += 1;
  }

  static void finish (Plugin plugin)
  {
    plugin.saveConfig ();
  }

  interface SpawnChicken {
    boolean spawn (Location location, ItemStack[] itemStacks, World world);
  }
  static boolean readAndSpawn (Plugin plugin, SpawnChicken m)
  {
    try
    {
      FileConfiguration config = plugin.getConfig ();
      List<Double> locList = (List<Double>) config.getList (tag_loc + counter);
      List<String> invList = (List<String>) config.getList (tag_inv + counter);
      String worldName = config.getString (tag_world + counter);
      counter += 1;
      if (locList == null ||
        invList == null ||
        worldName == null)
      {
        return false;
      }
      World world = plugin.getServer ().getWorld (worldName);
      return m.spawn (toLocation (locList, world), toItemStack (invList), world);
    }
    catch (Exception e)
    {
      e.printStackTrace ();
      return false;
    }
  }
}
