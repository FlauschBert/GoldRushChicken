package org.flauschhaus.goldrushchicken;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin
{
  public static Logger logger;
  private static List<UUID> chickens = new ArrayList<UUID> ();

  public static void addChicken (UUID uuid)
  {
    chickens.add (uuid);
  }
  public static void removeChicken (UUID uuid)
  {
    chickens.remove (uuid);
  }

  private void setupStorage ()
  {
    try
    {
      // Create data folder inside plugin data section
      // if not available
      if (!getDataFolder ().exists ())
      {
        getDataFolder ().mkdirs ();
      }

      // Create "config.yml" if not available
      saveDefaultConfig ();
    }
    catch (Exception e)
    {
      e.printStackTrace ();
    }
  }

  @Override
  public void onLoad ()
  {
    // Provide logger to plugin classes
    logger = this.getLogger ();
  }

  @Override
  public void onEnable ()
  {
    setupStorage ();

    // Register entity here so the server isn't able to instantiate
    // our entities from a past session - we do this ourselves to be
    // able to fill the chicken's bag
    EntityTypes type = EntityTypes.GOLD_RUSH_CHICKEN;
    logger.info ("Registering Entity Type " + type);

    // Register our command "kit" (set an instance of your command class as executor)
    getCommand("grc").setExecutor(new Command());

    // load and spawn chicken

    // Register Event handler
    getServer ().getPluginManager ().registerEvents (new EventHandler (),this);
  }

  @Override
  public void onDisable ()
  {
    // Save and destroy all gold rush chicken - nobody is harmed ... only sleeping ^^
    Storage.reset (Storage.Mode.WRITE, this);
    for (UUID uuid : chickens)
    {
      CraftChicken craftChicken = (CraftChicken) Bukkit.getEntity (uuid);
      Location location = craftChicken.getLocation ();
      GoldRushChicken chicken = (GoldRushChicken) craftChicken.getHandle ();
      ItemStack[] itemStacks = chicken.getInventory ().getStorageContents ();
      Storage.addToSave (location, itemStacks, this);
      craftChicken.remove ();
    }
    Storage.finishSave (this);
  }
}
