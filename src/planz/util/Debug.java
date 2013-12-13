package planz.util;

import java.lang.reflect.*;
import java.util.*;

@SuppressWarnings({"rawtypes"})
public class Debug 
{
    public Debug() {  }
    
    public static String toString(Object val)
    {
        if (val == null)
            return "null";
            
        String retVal = "";
        Class<?> cls = val.getClass();
        
        if (cls.isArray())
        {
            retVal += "{ ";
            
            String strTmp = "";
            int len = Array.getLength(val);
            
            for (int idx=0; idx<len; idx++)
            {
                if (strTmp.length() > 0)
                    strTmp += ", ";
                
                Object arrVal = Array.get(val, idx);
                
                if (arrVal == null)
                    strTmp += "null";
                else
                    strTmp += toString(arrVal);
            }
            
            retVal += strTmp + " }";
        }
        else
        {
            String clsName = cls.getName();
            
            if (clsName.equals("java.lang.String"))
                retVal += (String)val;
            else
            {
                retVal += val.toString() + "(" + cls.getName() + ")";
            }
        }
        
        return retVal;
    }
    
    public static void toLog(Hashtable<?,?> ctx)
    {
        String strLog="\n-》Hashtable view ----------------------------------------------\n";
        if (ctx == null)
        {
            //log.logDebug(strLog + "Hashtable handle is null !!!!!");
            return;
        }

        Enumeration<?> keys = ctx.keys();
                
        while(keys.hasMoreElements())
        {
            String key = keys.nextElement().toString();
            Object val = ctx.get(key);
            
            strLog += "> " + key + "\t: " + toString(val) + "\n";
        }
        
        //log.logDebug(strLog);
    }

    public static void toLog(Map ctx)
    {
        String strLog="\n-》Map view ----------------------------------------------\n";

        //log.logDebug(strLog+Debug.toLog(ctx, 0));
        
    }

    public static String toLog(Map ctx, int level)
    {        
        if (ctx == null)
            return "Map handle is null !!!!!";
        
        Set set = ctx.keySet();
        String[] keys = (String[])set.toArray(new String[set.size()]);
        String key = "", strLog = "";
        String pad="", padStr = ">>";
        
        for(int j=0; j<=level; j++) pad+=padStr;
        
        for(int i=0; i<keys.length; i++)
        {
            key = keys[i];
            Object val = ctx.get(keys[i]);
            
            if (val instanceof Map)
            {
                strLog += pad + " " + key + "\t: (" + val.getClass().getSimpleName() +")\n";
                strLog += Debug.toLog((Map)val, level+1);                
            }
            else if (val instanceof Map[])
            {
                for (Map map : (Map[])val)
                {
                    strLog += pad + " " + key + "\t: (" + val.getClass().getSimpleName() +")\n";
                    strLog += Debug.toLog(map, level+1);
                }
            }
            else
            {
                //if (val != null)
                //    strLog += pad + " >> " + val.getClass().getSimpleName() + " << ";
                
                strLog += pad + " " + key + "\t: " + toString(val) + "\n";
            }
        }
        
        return strLog + "\n";
    }
}