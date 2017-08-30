package org.flauschhaus.goldrushchicken;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin
{
  Logger logger;

  @Override
  public void onLoad ()
  {
    // Save logger
    logger = this.getLogger ();

    // Register entity here already so the server is able to instantiate
    // our entities from a past session
    EntityTypes type = EntityTypes.GOLD_RUSH_CHICKEN;
    logger.info ("Registering Entity Type " + type);
  }

  @Override
  public void onEnable ()
  {
    // Register our command "kit" (set an instance of your command class as executor)
    this.getCommand("grc").setExecutor(new Command());
  }

  @Override
  public void onDisable ()
  {
    // Fired when the server stops and disables all plugins
  }
}
