package planz.core.parser;

import java.util.*;

@SuppressWarnings("rawtypes")
public class Json
{
    public static String Append(String json, String key, Object value)
    {
        if (json == null)       json = "";
        if (json.length() > 0)  json += ",";
        
        if (key != null && key.length()>0)
            json += key + ":";
        
        if (value == null)
        {
            json += "null";
            return json;
        }
        
        Class<?> cls  = value.getClass();
        
        if (value instanceof Map)
            json += "{" + Json.FromMap((Map)value) + "}";
        else if (cls.isArray())
            json += "[" + Json.FromArray((Object[])value) + "]";
        else
            json += "'" + String.valueOf(value).replaceAll("\\'", "\\\\'") + "'";
        
        return json;
    }
    
    public static String Append(String json, Object value)
    {
        return Json.Append(json, "", value);
    }
    
    public static String FromArray(Object[] array)
    {
        String json = "";
        
        for (Object value: array)
            json = Json.Append(json, value);

        return json;
    }
    
    public static String FromMap(Map map)
    {
        String json = "";
        
        Set keys = map.keySet();
        
        for (Iterator itor = keys.iterator(); itor.hasNext();)
        {
            String key = (String)itor.next();            
            Object val = map.get(key);
            
            json = Json.Append(json, key, val);
        }
        
        return json;
    }
    
    public static Map FromJson(String json)
    {
        return null;
    }
}
