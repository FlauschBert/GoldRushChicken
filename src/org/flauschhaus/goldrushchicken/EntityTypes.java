package org.flauschhaus.goldrushchicken;

import net.minecraft.server.v1_12_R1.Entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public enum EntityTypes
{
  SKELETAL_DIGGER ("skeletal_digger", 5, SkeletalDigger.class, "SkeletalDigger");
  //GOLD_RUSH_CHICKEN ("gold_rush_chicken", 201, GoldRushChicken.class, "GoldRushChicken");

  EntityTypes(String internalName, int id, Class<? extends Entity> className, String description)
  {
    Method registerType = getRegisterTypeMethod ();
    Method registerBehaviour = getRegisterBehaviourMethod ();
    try
    {
      // Register entity class
      registerType.invoke (null, id, internalName, className, description);
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
