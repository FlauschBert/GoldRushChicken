package org.flauschhaus.goldrushchicken;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
      chicken.openInventory ((CraftPlayer) event.getPlayer ());
    }
    catch (ClassCastException e)
    {
      event.setCancelled (true);
    }
  }
}
