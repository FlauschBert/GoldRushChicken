package org.flauschhaus.goldrushchicken;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Helper
{
  public static Object getPrivateMember (String fieldName, Class clazz, Object object)
  {
    try
    {
      Field field = clazz.getDeclaredField (fieldName);
      field.setAccessible (true);
      return field.get (object);
    } catch (NoSuchFieldException | IllegalAccessException e)
    {
      e.printStackTrace ();
    }
    return null;
  }
}
