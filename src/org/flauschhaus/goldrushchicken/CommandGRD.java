package org.flauschhaus.goldrushchicken;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

public class CommandGRD implements CommandExecutor
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
    if (SkeletalDigger.spawn (location, (CraftWorld) world))
    {
      commandSender.sendMessage ("Spawning skeletal digger...");
    }
    else
    {
      commandSender.sendMessage ("Spawning skeletal digger failed :(");
    }
    return true;
  }

}
