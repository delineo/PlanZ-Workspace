package planz.db.statement;

import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;


/**
* This class wraps around a {@link PreparedStatement} and allows the programmer
* to set parameters by name instead of by index.  
* This eliminates any confusion as to which parameter index represents what.
* This also means that rearranging the SQL statement or adding a parameter 
* doesn't involve renumbering your indices.
* 
* Code such as this:
*
* Connection con=getConnection();
* String query="select * from my_table where name=? or address=?";
* PreparedStatement p=con.prepareStatement(query);
* p.setString(1, "bob");
* p.setString(2, "123 terrace ct");
* ResultSet rs=p.executeQuery();
*
* can be replaced with:
*
* Connection con=getConnection();
* String query="select * from my_table where name=:name or address=:address";
* NamedParameterStatement p=new NamedParameterStatement(con, query);
* p.setString("name", "bob");
* p.setString("address", "123 terrace ct");
* ResultSet rs=p.executeQuery();
*
* @author adam_crume
*/
@SuppressWarnings("rawtypes")
public class NamedStatement
{
    /** The statement this object is wrapping. */
    private enum StatementType
    {
        Prepared, Callable
    }
    private enum ParamType
    {
        In(0x01), Out(0x02), InOut(0x03);
        public final int Value;
        private ParamType(int value)
        {
            Value = value;
        }
        public boolean Contain(ParamType val)
        {
            return (Value & val.Value)>0;
        }
    };

    private final class Index
    {
        public int Index;
        public ParamType Type;
        
        public Index(int index, ParamType type)
        {
            this.Index = index;
            this.Type  = type;
        }
    }

    private final PreparedStatement _statement;
    private StatementType   _type = StatementType.Prepared;
    private String _query;

    private final Map<String, Set<Index>> _paraIdx;
    private       Map<String, Object>     _inVal;
    private       Map<String, Object>     _outKey;
    private       Map<String, Object>     _outVal;

    protected int _batchCnt = 0;


    /**
     * Creates a NamedParameterStatement. Wraps a call to
     * c.{@link Connection#prepareStatement(java.lang.String) prepareStatement}.
     * @param connection the database connection
     * @param query      the parameterized query
     * @throws SQLException if the statement could not be created
     */
    public NamedStatement(Connection connection, StringBuffer query)
        throws SQLException
    {
        this(connection, query.toString());
    }

    public NamedStatement(Connection connection, String query)
        throws SQLException
    {
        _paraIdx = new HashMap<String, Set<Index>>();
        _inVal   = new HashMap<String, Object    >();
        _outKey  = new HashMap<String, Object    >();
        _outVal  = new HashMap<String, Object    >();

        _query   = query;

        String parsedQuery=parse(query);

        if (_type.equals(StatementType.Prepared))
            _statement=connection.prepareStatement(parsedQuery);
        else
            _statement=connection.prepareCall(parsedQuery);
        
        _statement.setFetchSize(500);
    }

    /**
     * Parses a query with named parameters. The parameter-index mappings are 
     * put into the map, and the parsed query is returned.  
     * @param query query to parse
     * @return the parsed query
     */
    private String parse(String query)
    {
        // I was originally using regular expressions, but they didn't work well 
        // for ignoring parameter-like strings inside quotes.
        int length=query.length();
        StringBuffer parsedQuery=new StringBuffer(length);
        boolean inSingleQuote=false;
        boolean inDoubleQuote=false;
        int index=1;

        _paraIdx.clear();
        _inVal.clear();
        _outKey.clear();

        for(int i=0;i<length;i++)
        {
            char c=query.charAt(i);
            if(inSingleQuote)
            {
                if(c=='\'')     inSingleQuote=false;
            }
            else if(inDoubleQuote)
            {
                if(c=='"' )     inDoubleQuote=false;
            }
            else if(c=='-' && i+1<length)
            {
                if (query.charAt(i+1) == '-')
                {// 한줄 주석부분
                    int j=i+1;
                    while(j<length && query.charAt(j) != '\n')
                    {
                        j++;
                    }
                    
                    String comment=query.substring(i,j);
                    parsedQuery.append(comment);
                    c='\n';
                    i+=comment.length(); // skip past the end if the parameter
                }
            }
            else
            {
                if(c=='\'' )    inSingleQuote=true;
                else if(c=='"') inDoubleQuote=true;
                else if(c==':' && i+1<length && Character.isJavaIdentifierStart(query.charAt(i+1)))
                {
                    int j=i+2;
                    while(j<length && (Character.isJavaIdentifierPart(query.charAt(j)) || query.charAt(j) == '.'))
                    {
                        j++;
                    }
                    String name=query.substring(i+1,j);
                    c='?'; // replace the parameter with a question mark
                    i+=name.length(); // skip past the end if the parameter

                    Set<Index> indexSet = _paraIdx.get(name);
                    if (indexSet==null)
                    {
                        indexSet=new HashSet<Index>();
                        _paraIdx.put(name, indexSet);
                    }
                    indexSet.add(new Index(index, ParamType.In));
                    index++;
                }
                else if(c=='@' && i+1<length && Character.isJavaIdentifierStart(query.charAt(i+1)))
                {
                    _type   = StatementType.Callable;

                    int j=i+2;
                    while(j<length && (Character.isJavaIdentifierPart(query.charAt(j)) || query.charAt(j) == '.'))
                    {
                        j++;
                    }
                    String name=query.substring(i+1,j);
                    c='?';
                    i+=name.length();

                    Set<Index> indexSet = _paraIdx.get(name);
                    if (indexSet==null)
                    {
                        indexSet=new HashSet<Index>();
                        _paraIdx.put(name, indexSet);
                    }
                    indexSet.add(new Index(index, ParamType.Out));
                    index++;
                }
                else if(c=='#' && i+1<length && Character.isJavaIdentifierStart(query.charAt(i+1)))
                {
                    _type   = StatementType.Callable;

                    int j=i+2;
                    while(j<length && (Character.isJavaIdentifierPart(query.charAt(j)) || query.charAt(j) == '.'))
                    {
                        j++;
                    }
                    String name=query.substring(i+1,j);
                    c='?';
                    i+=name.length();

                    Set<Index> indexSet = _paraIdx.get(name);
                    if (indexSet==null)
                    {
                        indexSet=new HashSet<Index>();
                        _paraIdx.put(name, indexSet);
                    }
                    indexSet.add(new Index(index, ParamType.InOut));
                    index++;
                }
            }
            parsedQuery.append(c);
        }

        return parsedQuery.toString();
    }


    /**
     * Returns the indexes for a parameter.
     * @param name parameter name
     * @return parameter indexes
     * @throws IllegalArgumentException if the parameter does not exist
     */
    private Set<Index> getIndexes(String name)
    {
        Set<Index> indexes = _paraIdx.get(name);
        if(indexes==null)
            throw new IllegalArgumentException("Parameter not found: "+name);

        return indexes;
    }


    /**
     * Sets a parameter.
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setObject(int, java.lang.Object)
     */
    public void setObject(String name, Object value)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
            {
                _statement.setObject(index.Index, value);
                _inVal.put(name, value);
            }

            if ((index.Type.Value & ParamType.Out.Value)>0)
            {
                String[] elem = name.split("\\.");
                
                int nType = Types.VARCHAR;

                if (elem.length > 1)
                {
                    String sType = elem[1].toUpperCase();
                    if ("".equals(sType) || "VARCHAR".equals(sType))
                        nType = Types.VARCHAR;
                    else if ("NUBMER".equals(sType))
                        nType = Types.NUMERIC;
                    else if ("BLOB".equals(sType))
                        nType = Types.BLOB;
                    else if ("CLOB".equals(sType))
                        nType = Types.CLOB;
                    else if ("CURSOR".equals(sType))
                        nType = Types.CURSOR;
                }

                ((CallableStatement)_statement).registerOutParameter(index.Index, nType);
                _outKey.put(name, null);
            }
        }
    }

    public Object getObject(String name)
        throws Exception
    {
        if (_outVal == null) return null;
        
        Object value = null;
        
        if (_outVal.containsKey(name))
            value = _outVal.get(name);

        return value;
    }

    /**
     * Sets a parameter.
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setString(int, java.lang.String)
     */
    public void setString(String name, String value)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);
        _inVal.put(name, value);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
                _statement.setString(index.Index, value);

            if ((index.Type.Value & ParamType.Out.Value)>0)
                ((CallableStatement)_statement).registerOutParameter(index.Index, Types.VARCHAR);
        }
    }
    public String getString(String name)
        throws Exception
    {
        if (_outVal == null) return null;
        
        Object value = null;
        
        if (_outVal.containsKey(name))
            value = _outVal.get(name);

        return String.valueOf(value);
    }


    /**
     * Sets a parameter.
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setInt(String name, int value)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);
        _inVal.put(name, value);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
                _statement.setInt(index.Index, value);

            if ((index.Type.Value & ParamType.Out.Value)>0)
                ((CallableStatement)_statement).registerOutParameter(index.Index, Types.INTEGER);
        }
    }
    public int getInt(String name)
        throws Exception
    {
        if (_outVal == null) return 0;
        
        Object value = null;
        
        if (_outVal.containsKey(name))
            value = _outVal.get(name);
    
        return (Integer)value;
    }

    /**
     * Sets a parameter.
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setInt(int, int)
     */
    public void setLong(String name, long value)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);
        _inVal.put(name, value);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
                _statement.setLong(index.Index, value);

            if ((index.Type.Value & ParamType.Out.Value)>0)
                ((CallableStatement)_statement).registerOutParameter(index.Index, Types.NUMERIC);
        }
    }
    public long getLong(String name)
        throws Exception
    {
        if (_outVal == null) return 0;
        
        Object value = null;
        
        if (_outVal.containsKey(name))
            value = _outVal.get(name);
    
        return (Long)value;
    }


    /**
     * Sets a parameter.
     * @param name  parameter name
     * @param value parameter value
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setTimestamp(int, java.sql.Timestamp)
     */
    public void setTimestamp(String name, Timestamp value)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);
        _inVal.put(name, value);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
                _statement.setTimestamp(index.Index, value);

            if ((index.Type.Value & ParamType.Out.Value)>0)
                ((CallableStatement)_statement).registerOutParameter(index.Index, Types.TIMESTAMP);
        }
    }

    /**
     * Sets a parameter to sql NULL
     * @param name  parameter name
     * @throws SQLException if an error occurred
     * @throws IllegalArgumentException if the parameter does not exist
     * @see PreparedStatement#setNull
     */
    public void setNull(String name)
        throws SQLException
    {
        Set<Index> indexes = getIndexes(name);
        _inVal.put(name, null);

        Index index = null;
        for (Iterator<Index> itr = indexes.iterator(); itr.hasNext();)
        {
            index = itr.next();
            
            if ((index.Type.Value & ParamType.In.Value)>0)
                _statement.setNull(index.Index, Types.NULL);

            if ((index.Type.Value & ParamType.Out.Value)>0)
                ((CallableStatement)_statement).registerOutParameter(index.Index, Types.NULL);
        }
    }

    public void setParameters(Map values)
        throws SQLException
    {
        this.setParameters(values, true);
    }
    public void setParameters(Map values, boolean clear)
        throws SQLException
    {
        if (values == null) return;

        String[] params = this.getParameterNames();
        
        if (clear) this.clearParameters();

        for(int idx=0; idx < params.length; idx++)
        {
            Object value = values.get(params[idx]);
            this.setObject(params[idx], value);
        }
    }

    public Map getOutputs()
    {
        return _outVal;
    }

    /**
     * Returns the underlying statement.
     * @return the statement
     */
    public PreparedStatement getStatement()
    {
        return _statement;
    }


    /**
     * Executes the statement.
     * @return true if the first result is a {@link ResultSet}
     * @throws SQLException if an error occurred
     * @see PreparedStatement#execute()
     */
    public boolean execute()
        throws SQLException
    {
        boolean bRet = false;
        
        if (_type.equals(StatementType.Callable))
        {

            CallableStatement stmt = (CallableStatement)_statement;
            bRet = stmt.execute();

            _outVal.clear();

            if (!_outKey.isEmpty())
            {
                Set<String> keys= _outKey.keySet();
                
                Set<Index> indexes = null; 
                String key = null;
                Index  idx = null;
                for (Iterator<String> iKeys = keys.iterator(); iKeys.hasNext();)
                {
                    key = iKeys.next();
                    indexes = getIndexes(key);
                    
                    for (Iterator<Index> iIdx = indexes.iterator(); iIdx.hasNext();)
                    {
                        idx = iIdx.next();
                        
                        String[] elem = key.split("\\.");

                        if (elem.length > 1)
                            key = elem[0];

                        _outVal.put(key, stmt.getObject(idx.Index));
                    }
                }
            }
            
        }
        else
            bRet = _statement.execute();
        
        return bRet;
    }


    /**
     * Executes the statement, which must be a query.
     * @return the query results
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeQuery()
     */
    public ResultSet executeQuery()
        throws SQLException
    {
        ResultSet rSet = null;
        
        if (_type.equals(StatementType.Callable))
        {

            CallableStatement stmt = (CallableStatement)_statement;
            rSet = stmt.executeQuery();

            _outVal.clear();

            if (!_outKey.isEmpty())
            {
                Set<String> keys= _outKey.keySet();
                
                Set<Index> indexes = null; 
                String key = null;
                Index  idx = null;
                for (Iterator<String> iKeys = keys.iterator(); iKeys.hasNext();)
                {
                    key = iKeys.next();
                    indexes = getIndexes(key);
                    
                    for (Iterator<Index> iIdx = indexes.iterator(); iIdx.hasNext();)
                    {
                        idx = iIdx.next();
                        
                        String[] elem = key.split("\\.");

                        if (elem.length > 1)
                            key = elem[0];

                        _outVal.put(key, stmt.getObject(idx.Index));
                    }
                }
            }
            
        }
        else
            rSet = _statement.executeQuery();
        
        return rSet;
    }


    /**
     * Executes the statement, which must be an SQL INSERT, UPDATE or DELETE
     * statement; or an SQL statement that returns nothing, such as a DDL 
     * statement.
     * @return number of rows affected
     * @throws SQLException if an error occurred
     * @see PreparedStatement#executeUpdate()
     */
    public int executeUpdate()
        throws SQLException
    {
        int nAffect = -1;
        
        if (_type.equals(StatementType.Callable))
        {

            CallableStatement stmt = (CallableStatement)_statement;
            nAffect = stmt.executeUpdate();

            _outVal.clear();

            if (!_outKey.isEmpty())
            {
                Set<String> keys= _outKey.keySet();
                
                Set<Index> indexes = null; 
                String key = null;
                Index  idx = null;
                for (Iterator<String> iKeys = keys.iterator(); iKeys.hasNext();)
                {
                    key = iKeys.next();
                    indexes = getIndexes(key);
                    
                    for (Iterator<Index> iIdx = indexes.iterator(); iIdx.hasNext();)
                    {
                        idx = iIdx.next();
                        
                        if (!idx.Type.Contain(ParamType.Out))
                            continue;
                        
                        String[] elem = key.split("\\.");

                        if (elem.length > 1)
                            key = elem[0];

                        _outVal.put(key, stmt.getObject(idx.Index));
                    }
                }
            }
            
        }
        else
            nAffect = _statement.executeUpdate();
        
        return nAffect;
    }


    /**
     * Closes the statement.
     * @throws SQLException if an error occurred
     * @see Statement#close()
     */
    public void close()
        throws SQLException
    {
        _paraIdx.clear();
        _inVal.clear();
        _statement.close();
    }


    /**
     * Adds the current set of parameters as a batch entry.
     * @throws SQLException if something went wrong
     */
    public void addBatch()
        throws SQLException
    {
        _batchCnt++;
        _statement.addBatch();
    }

    public int[] addBatch(int limit)
        throws SQLException
    {
        _batchCnt++;
        _statement.addBatch();

        if (_batchCnt >= limit)
        {
            //logger.logDebug(">> Execute Batch by limit (" + String.valueOf(limit) + ").");

            int[] ret = _statement.executeBatch();
            clearBatch();

            return ret;
        }

        return new int[] {};
    }
    
    public int getBatchCount()
    {
        return _batchCnt;
    }

    /**
     * Executes all of the batched statements.
     * 
     * See {@link Statement#executeBatch()} for details.
     * @return update counts for each statement
     * @throws SQLException if something went wrong
     */
    public int[] executeBatch()
        throws SQLException
    {
        //logger.logDebug(">> Execute Batch.........");
        
        int ret[] = _statement.executeBatch();
        /*
        for(int idx=0; idx < ret.length; idx++)
        {
            logger.logDebug(">> Batch RESULT : " + ret[idx]);
        }
        */
        // Statement.EXECUTE_FAILED -3;
        
        return ret;
    }
    
    public void clearBatch()
        throws SQLException 
    {
        _batchCnt = 0;
        _statement.clearBatch();
    }
    
    public int getParameterCount()
    {
        return _paraIdx.size();
    }
    
    public String[] getParameterNames()
    {
        Set<String> keys = _paraIdx.keySet();
        if (keys == null) return new String[0];

        String[] array = new String[keys.size()];
        int idx = 0;
        for (Iterator<String> itr = keys.iterator(); itr.hasNext();)
            array[idx++] = itr.next();

        return array;
    }
    
    public void clearParameters()
        throws SQLException
    {
        _inVal.clear();
        _outKey.clear();
        _outVal.clear();
        _statement.clearParameters();
    }
   
    public void clearWarnings()
        throws SQLException
    {
        _statement.clearWarnings();
    }
    
    public int getUpdateCount()
        throws SQLException
    {
        return _statement.getUpdateCount();
    }
    
    public boolean getMoreResults()
        throws SQLException
    {
        return _statement.getMoreResults();
    }
    
    public boolean getMoreResults(int current)
        throws SQLException
    {
        return _statement.getMoreResults(current);
    }
    
    public ResultSet getResultSet()
        throws SQLException
    {
        return _statement.executeQuery();
    }

    public List getResultList()
        throws SQLException
    {
        ResultSet rset = _statement.executeQuery();
        
        if (rset == null) return null;
        
        ResultSetMetaData meta = rset.getMetaData();
        ArrayList<HashMap<?, ?>> list = new ArrayList<HashMap<?, ?>>();
        while(rset.next())
        {
            HashMap<Object,Object> map = new HashMap<Object,Object>();
            for(int colInd = 1; colInd <= meta.getColumnCount(); colInd++)
            {
                map.put(meta.getColumnName(colInd), rset.getObject(colInd));
            }
            
            list.add(map);
        }

        //logger.logDebug(">>>>> Record Count : " + list.size());

        try { rset.close(); } catch (Exception none) {};

        return list;
    }
    
    public String toString()
    {
        String query = _query;

        Set<String> keys = _paraIdx.keySet();
        for (Iterator<String> itr = keys.iterator(); itr.hasNext();)
        {
            String paraName = itr.next();
            Object value = _inVal.get(paraName);
            paraName = ":"+paraName;

            if (value == null)
                query = query.replace(paraName, "null");
            else if (value instanceof String || value instanceof Character)
                query = query.replace(paraName, "'" + String.valueOf(value).replaceAll("'", "''") + "'");
            else if (value instanceof Integer || value instanceof Long || value instanceof Double || value instanceof Float)
                query = query.replace(paraName, String.valueOf(value));
            else if (value instanceof Date)
            {
                SimpleDateFormat format = new SimpleDateFormat("'yyyy-MM-dd HH:mm:ss'");
                query = query.replace(paraName, format.format((Date)value));
            }
            else if (value instanceof Timestamp)
            {
                SimpleDateFormat format = new SimpleDateFormat("'yyyy-MM-dd HH:mm:ss'");
                query = query.replace(paraName, format.format((Timestamp)value));
            }
        }

        return query.replaceAll("[ \t]+\n", "\n");
    }
    
    protected abstract class Types
    {
        /* java.sql.types */
        public static final int BIT             = -7;
        public static final int TINYINT         = -6;
        public static final int SMALLINT        = 5;
        public static final int INTEGER         = 4;
        public static final int BIGINT          = -5;
        public static final int FLOAT           = 6;
        public static final int REAL            = 7;
        public static final int DOUBLE          = 8;
        public static final int NUMERIC         = 2;
        public static final int DECIMAL         = 3;
        public static final int CHAR            = 1;
        public static final int VARCHAR         = 12;
        public static final int LONGVARCHAR     = -1;
        public static final int DATE            = 91;
        public static final int TIME            = 92;
        public static final int TIMESTAMP       = 93;
        public static final int BINARY          = -2;
        public static final int VARBINARY       = -3;
        public static final int LONGVARBINARY   = -4;
        public static final int NULL            = 0;
        public static final int OTHER           = 1111;
        public static final int JAVA_OBJECT     = 2000;
        public static final int DISTINCT        = 2001;
        public static final int STRUCT          = 2002;
        public static final int ARRAY           = 2003;
        public static final int BLOB            = 2004;
        public static final int CLOB            = 2005;
        public static final int REF             = 2006;
        public static final int DATALINK        = 70;
        public static final int BOOLEAN         = 16;
        public static final int ROWID           = -8;
        public static final int NVARCHAR        = -9;
        public static final int LONGNVARCHAR    = -16;
        public static final int NCLOB           = 2011;
        public static final int SQLXML          = 2009;

        /* OracleTypes */
        public static final int TIMESTAMPNS     = -100;
        public static final int TIMESTAMPTZ     = -101;
        public static final int TIMESTAMPLTZ    = -102;
        public static final int INTERVALYM      = -103;
        public static final int INTERVALDS      = -104;
        public static final int CURSOR          = -10;
        public static final int BFILE           = -13;
        public static final int OPAQUE          = 2007;
        public static final int JAVA_STRUCT     = 2008;
        public static final int PLSQL_INDEX_TABLE=-14;
        public static final int BINARY_FLOAT    = 100;
        public static final int BINARY_DOUBLE   = 101;
        public static final int NUMBER          = 2;
        public static final int RAW             = -2;
        public static final int FIXED_CHAR      = 999;
    }    
}