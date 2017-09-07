package org.flauschhaus.goldrushchicken;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin
{
  static Logger logger;
  static Plugin plugin;
  private static List<UUID> chickens = new ArrayList<> ();

  static void addChicken (UUID uuid)
  {
    chickens.add (uuid);
  }
  static void removeChicken (UUID uuid)
  {
    chickens.remove (uuid);
  }

  @Override
  public void onLoad ()
  {
    // Provide logger to plugin classes
    logger = this.getLogger ();
    plugin = this;
  }

  @Override
  public void onEnable ()
  {
    Storage.init (this);

    // Register entity here so the server isn't able to instantiate
    // our entities from a past session - we do this ourselves to be
    // able to fill the chicken's bag
    EntityTypes type1 = EntityTypes.GOLD_RUSH_CHICKEN;
    logger.info ("Registering Entity Type " + type1);
    EntityTypes type2 = EntityTypes.SKELETAL_DIGGER;
    logger.info ("Registering Entity Type " + type2);

    // Register our command "kit" (set an instance of your command class as executor)
    getCommand("grc").setExecutor(new CommandGRC ());
    getCommand("grd").setExecutor(new CommandGRD ());

    // load and spawn chicken
    Storage.reset ();
    while (Storage.readAndSpawn (this));
    Storage.finish (this);

    // Register Event handler
    getServer ().getPluginManager ().registerEvents (new EventHandler (),this);
  }

  @Override
  public void onDisable ()
  {
    // Save and destroy all gold rush chicken - nobody is harmed ... only sleeping ^^
    Storage.reset ();
    for (UUID chicken : chickens)
    {
      logger.info ("Saving chicken " + chicken.toString ());
      saveAndRemove (chicken);
    }
    Storage.finish (this);
  }

  private void saveAndRemove (UUID chicken)
  {
    CraftChicken craftChicken = (CraftChicken) Bukkit.getEntity (chicken);
    GoldRushChicken grChicken = (GoldRushChicken) craftChicken.getHandle ();
    ItemStack[] itemStacks = grChicken.getInventory ().getStorageContents ();
    Storage.add (craftChicken.getLocation (), itemStacks, craftChicken.getWorld (), this);
    craftChicken.remove ();
  }
}
