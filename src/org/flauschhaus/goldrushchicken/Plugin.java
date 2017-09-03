package org.flauschhaus.goldrushchicken;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Plugin extends JavaPlugin
{
  public static Logger logger;

  @Override
  public void onLoad ()
  {
    // Provide logger to plugin classes
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
    getCommand("grc").setExecutor(new Command());

    // Register Event handler
    getServer ().getPluginManager ().registerEvents (new EventHandler (),this);
  }

  @Override
  public void onDisable ()
  {
  }
}
