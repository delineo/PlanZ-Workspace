package planz.config;

import java.lang.reflect.*;

import org.apache.log4j.*;

import planz.db.*;
import planz.util.*;

public class SysConfig extends HashMapEx
{
    static Logger log = Logger.getLogger(SysConfig.class);
    
    static SysConfig _instance = new SysConfig();
    public static SysConfig getInstance() { return _instance; }

    private void loadConfig()
    {
        DbManager mgrDb = DbManager.getInstance();
        
        log.debug(">> Load System configuration....");
        
        
    }

    public SysConfig()
    {
        loadConfig();
    }
    
    public static<T> T get(String name)
    {
        SysConfig self = SysConfig.getInstance();
        Object value = null;
        
        try
        {
            Class<HashMapEx> parent = (Class<HashMapEx>)self.getClass().getSuperclass();
            Method mth = parent.getMethod("get", HashMapEx.class);
            value = mth.invoke(self, name);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return (T)value;
    }
    
    public static void put(String name, Object value)
    {
        SysConfig self = SysConfig.getInstance();
        try
        {
            Class<HashMapEx> parent = (Class<HashMapEx>)self.getClass().getSuperclass();
            Method mth = parent.getMethod("put", HashMapEx.class);
            mth.invoke(self, name, value);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
