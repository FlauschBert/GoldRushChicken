package org.flauschhaus.goldrushchicken;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Storage
{
  private static final String tag_inv = "inv";
  private static final String tag_loc = "loc";

  private static int counter = 0;

  private static List<String> serialize (ItemStack[] itemStacks)
  {
    List<String> result = new ArrayList<String> ();
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

  private static List<Double> serialize (Location location)
  {
    List<Double> result = new ArrayList<Double> ();
    result.add (location.getX ());
    result.add (location.getY ());
    result.add (location.getZ ());
    result.add ((double)location.getPitch ());
    result.add ((double)location.getYaw ());
    return result;
  }

  enum Mode { READ, WRITE };
  public static void reset (Mode mode, Plugin plugin)
  {
    if (mode == Mode.WRITE)
    {
      // Clear config file
      plugin.saveResource ("config.yml", true);
    }

    counter = 0;
  }

  public static void addToSave (Location location, ItemStack[] itemStacks, Plugin plugin)
  {
    plugin.getConfig ().set (tag_loc + counter, serialize (location));
    plugin.getConfig ().set (tag_inv + counter, serialize (itemStacks));
    counter += 1;
  }

  public static void finishSave (Plugin plugin)
  {
    plugin.saveConfig ();
  }
}
