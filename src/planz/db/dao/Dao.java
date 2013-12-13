package planz.db.dao;

import java.sql.*;
import java.util.*;

import planz.db.*;
import planz.db.statement.*;

@SuppressWarnings("rawtypes")
public class Dao
{
    protected DbManager      _mgrDb = DbManager.getInstance();
    protected Connection     _conn  = null;
    protected NamedStatement _stmt  = null;
    protected ResultSet      _rs    = null;

    /* 
     * 생성자
     */
    public Dao(String dsName)
        throws SQLException
    {
        _conn = _mgrDb.getConnection(dsName);
    }

    public Dao(String dsName, String sql)
        throws SQLException
    {
        _conn = _mgrDb.getConnection(dsName);
        _stmt = new NamedStatement(_conn, sql);
    }
    
    public Dao(String dsName, String sql, Map<String, ?> params)
        throws SQLException
    {
        _conn = _mgrDb.getConnection(dsName);
        _stmt = new NamedStatement(_conn, sql);
        _stmt.setParameters(params);
    }

    /* 
     * 종료 함수
     */
    public void close()
    {
        try
        {
            _rs.close();
        }
        catch(Exception ex) {}
        finally { _rs = null; }

        try
        {
            if (_stmt != null) _stmt.close();
            _stmt = null;
        }
        catch(Exception ex) {}
        finally { _stmt = null; }
        
        try
        {
            if (_conn != null) _conn.close();
        }
        catch(Exception ex) {}
        finally { _conn = null; }
    }
    
    /* 
     * 파라메타 설정
     */
    public void setObject(String name, Object value) throws SQLException
    {
        _stmt.setObject(name, value);
    }

    public void setString(String name, String value) throws SQLException
    {
        _stmt.setString(name, value);
    }

    public void setInt(String name, int value) throws SQLException
    {
        _stmt.setInt(name, value);
    }

    public void setLong(String name, long value) throws SQLException
    {
        _stmt.setLong(name, value);
    }

    public void setTimestamp(String name, Timestamp value) throws SQLException
    {
        _stmt.setTimestamp(name, value);
    }

    public void setNull(String name) throws SQLException
    {
        _stmt.setNull(name);
    }
    
    public void setParameters(Map<String, ?> values) throws SQLException
    {
        _stmt.setParameters(values);
    }

    public void setParameters(Map<String, ?> values, boolean clear) throws SQLException
    {
        _stmt.setParameters(values, clear);
    }

    public int getParameterCount()
    {
        return _stmt.getParameterCount();
    }

    public String[] getParameterNames()
    {
        return _stmt.getParameterNames();
    }

    public void clearParameters() throws SQLException
    {
        _stmt.clearParameters();
    }
    /*---------------------------------------------------------------------------------------------------------------*/

    /* 
     * 쿼리 실행
     */
    public boolean execute() throws SQLException
    {
        return _stmt.execute();
    }
    
    public ResultSet executeQuery()
        throws SQLException
    {
        try
        {
            if (_rs != null) _rs.close();
        }
        catch(Exception ex) {}
        finally { _rs = null; }

        _rs = _stmt.executeQuery();
        return _rs;
    }

    public int executeUpdate()
        throws SQLException
    {
        return _stmt.executeUpdate();
    }

    public PreparedStatement getStatement()
    {
        return _stmt.getStatement();
    }

    public String getQueryStatement() throws SQLException
    {
        return ""; //_stmt.getQueryStatement();
    }

    public void clearWarnings() throws SQLException
    {
        _stmt.clearWarnings();
    }
    
    public int getUpdateCount() throws SQLException
    {
        return _stmt.getUpdateCount();
    }
    
    public boolean getMoreResults() throws SQLException
    {
        return _stmt.getMoreResults();
    }
    
    public boolean getMoreResults(int current) throws SQLException
    {
        return _stmt.getMoreResults(current);
    }
    
    public ResultSet getResultSet() throws SQLException
    {
        return _stmt.getResultSet();
    }
    
    public List getResultList() throws SQLException
    {
        return _stmt.getResultList();
    }
    /*---------------------------------------------------------------------------------------------------------------*/
    
    /* 
     * 배치 실행
     */
    public void addBatch()
        throws SQLException
    {
        _stmt.addBatch();
    }

    public int[] addBatch(int limit)
        throws SQLException
    {
        return _stmt.addBatch(limit);
    }
    
    public int getBatchCount()
    {
        return _stmt.getBatchCount();
    }

    public int[] executeBatch()
        throws SQLException
    {
        return _stmt.executeBatch();
    }
    
    public void clearBatch()
        throws SQLException 
    {
        _stmt.clearBatch();
    }
    
    /*---------------------------------------------------------------------------------------------------------------*/
}
