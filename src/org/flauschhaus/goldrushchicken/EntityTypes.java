package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public enum EntityTypes
{
  GOLD_RUSH_CHICKEN ("gold_rush_chicken", 93, GoldRushChicken.class, "GoldRushChicken");
  //GOLD_RUSH_CHICKEN ("gold_rush_chicken", 201, GoldRushChicken.class, "GoldRushChicken");

  EntityTypes(String internalName, int id, Class<? extends Entity> className, String description)
  {
    Method registerType = getRegisterTypeMethod ();
    Method registerBehaviour = getRegisterBehaviourMethod ();
    try
    {
      // Register entity class
      registerType.invoke (null, id, internalName, className, description);
      // Attributes that cow and chicken have (Eggs + ?)
      registerBehaviour.invoke (null, internalName, 10592673, 16711680);
    } catch (IllegalAccessException | InvocationTargetException e)
    {
      e.printStackTrace ();
    }
  }

  private Method getRegisterTypeMethod ()
  {
    try
    {
      Method m = net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredMethod("a", Integer.TYPE, String.class, Class.class, String.class);
      m.setAccessible(true);
      return m;
    } catch (NoSuchMethodException | SecurityException e)
    {
      e.printStackTrace ();
    }
    return null;
  }
  private Method getRegisterBehaviourMethod ()
  {
    try
    {
      Method m = net.minecraft.server.v1_12_R1.EntityTypes.class.getDeclaredMethod ("a", String.class, Integer.TYPE, Integer.TYPE);
      m.setAccessible(true);
      return m;
    } catch (NoSuchMethodException | SecurityException e)
    {
      e.printStackTrace ();
    }
    return null;
  }
}
