package planz.db;

import java.sql.*;
import java.util.*;

import javax.naming.*;
import javax.sql.*;

import org.apache.log4j.*;

public class DbManager
{
    static Logger log = Logger.getLogger(DbManager.class);
    
    static DbManager _instance = new DbManager();
    public static DbManager getInstance() { return _instance; }

    public static  String SysDsName   = "jdbc/planz";
    
    // Connection 정보 저장소
    // Db Connection을 저장하기위한 저장소
    private HashMap<String, DataSource> _ds = new HashMap<String, DataSource>();
    private String _defaultName = "";

    public void setDefaultDataSource(String name)
    {
        _defaultName = name;
    }
    
    public Connection getSysConnection()
    {
        return getConnection(DbManager.SysDsName);
    }
    
    public Connection getConnection(String name)
    {
        Connection conn= null;
        DataSource ds  = null;

        try
        {
            if (!_ds.containsKey(name))
            {
                Context ctxInit = new InitialContext();
                Context ctxEnv  = (Context)ctxInit.lookup("java:comp/env");
                
                ds   = (DataSource)ctxEnv.lookup(name);
                
                _ds.put(name, ds);
            }
            else
                ds = _ds.get(name);
            
            conn = ds.getConnection();
            conn.setAutoCommit(false);
        }
        catch (NamingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return conn;
    }
}
