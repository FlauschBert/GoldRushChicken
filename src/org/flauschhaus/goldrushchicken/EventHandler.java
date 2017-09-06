package org.flauschhaus.goldrushchicken;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EventHandler implements Listener
{
  @org.bukkit.event.EventHandler
  public void onOpenChickenChest (PlayerInteractEntityEvent event)
  {
    // Called twice on right click: We accept only OFF_HAND
    if (event.getHand () == EquipmentSlot.HAND)
      return;

    try
    {
      // Do we have chicken?
      CraftChicken craftChicken = (CraftChicken) event.getRightClicked ();
      // Do we have gold rush chicken?
      GoldRushChicken chicken = (GoldRushChicken) craftChicken.getHandle ();
      CraftPlayer craftPlayer = (CraftPlayer) event.getPlayer ();
      craftPlayer.openInventory (chicken.getInventory ());
    }
    catch (Exception e)
    {
      event.setCancelled (true);
    }
  }

  @org.bukkit.event.EventHandler
  public void onChickenGetsFried (EntityDeathEvent event)
  {
    try
    {
      // Do we have chicken?
      CraftChicken craftChicken = (CraftChicken) event.getEntity ();
      // Do we have gold rush chicken?
      GoldRushChicken chicken = (GoldRushChicken) craftChicken.getHandle ();
      // Remove from tracking
      Plugin.removeChicken (chicken.getUniqueID ());
    }
    catch (Exception e)
    {
      // Nothing to do
    }
  }

  @org.bukkit.event.EventHandler
  public void onPlayerLogin (PlayerLoginEvent event)
  {
    Bukkit.getScheduler ().scheduleSyncDelayedTask (Plugin.plugin, new Runnable ()
    {
      @Override
      public void run ()
      {
        // Try to spawn delayed chicken on every login
        GoldRushChicken.spawnDelayed (event.getPlayer ().getWorld ());
      }
    }, 20*5);
  }
}
