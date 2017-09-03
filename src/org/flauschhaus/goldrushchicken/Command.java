package org.flauschhaus.goldrushchicken;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class Command implements CommandExecutor
{
  private Location getPlayerLocation (CommandSender commandSender)
  {
    Server server = commandSender.getServer ();
    return server.getPlayer (commandSender.getName ()).getLocation ();
  }

  private World getWorld (CommandSender commandSender)
  {
    Server server = commandSender.getServer ();
    return server.getPlayer (commandSender.getName ()).getWorld ();
  }

  @Override
  public boolean onCommand (CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings)
  {
    Location location = getPlayerLocation (commandSender);
    location.add (1.0, 0.0, 0.0);

    World world = getWorld (commandSender);
    CraftWorld craftWorld = (CraftWorld) world;

    GoldRushChicken chicken = new GoldRushChicken (craftWorld.getHandle ());
    chicken.setLocation (location.getX(), location.getY(), location.getZ(),
                         location.getYaw(), location.getPitch());
    if (craftWorld.getHandle().addEntity(chicken))
    {
      commandSender.sendMessage ("Spawning gold rush chicken...");
      Plugin.addChicken (chicken.getUniqueID ());
      Plugin.logger.info (commandSender.getName () + " spawns gold rush chicken: " + chicken.getUniqueID ());
    }
    else
    {
      commandSender.sendMessage ("Spawning gold rush chicken failed :(");
    }
    return true;
  }
}
