package planz.core.variant;

import java.util.*;

public class Value 
{
    private Object    _value = null;
    private ValueType _type  = null;

    @SuppressWarnings("serial")
    private static final Map<String, ValueType> _types = new HashMap<String, ValueType>()
    {{  // 지원가능한 데이터 유형
        for (ValueType type : ValueType.values())
        {
            put(type.getClassName(), type);
        }
    }};

    /*
     * 생성자
     */
    public Value(Object value)
    {
        try 
        { 
            setValue(value);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        } 
    }
    public Value(boolean value) { this((Object)value); }
    public Value(byte    value) { this((Object)value); }
    public Value(short   value) { this((Object)value); }
    public Value(int     value) { this((Object)value); }
    public Value(long    value) { this((Object)value); }
    public Value(float   value) { this((Object)value); }
    public Value(double  value) { this((Object)value); }
    public Value(char    value) { this((Object)value); }
    public Value(String  value)
    {
        this((Object)value);
        
        try
        {
            if (isInteger())
                setValue(toInteger());
            else if (isReal())
                setValue(toDouble());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        } 
    }
    public Value(StringBuffer value) { this(value.toString()); }

    /*
     * 등록된 값을 반환한다.
     */
    public Object getValue()
    {
        return _value;
    }

    /*
     * 값을 지정한다. 
     */
    public void setValue(Object value)
        throws Exception
    {
        _value = assertType(value);
        _type  = _types.get(value==null?ValueType.NULL:value.getClass().getName());
    }
    public void setValue(boolean value) throws Exception { setValue((Object)value); }
    public void setValue(byte    value) throws Exception { setValue((Object)value); }
    public void setValue(short   value) throws Exception { setValue((Object)value); }
    public void setValue(int     value) throws Exception { setValue((Object)value); }
    public void setValue(long    value) throws Exception { setValue((Object)value); }
    public void setValue(float   value) throws Exception { setValue((Object)value); }
    public void setValue(double  value) throws Exception { setValue((Object)value); }
    public void setValue(char    value) throws Exception { setValue((Object)value); }
    public void setValue(String  value) throws Exception { setValue((Object)value); }

    /*
     * Null 값 여부 검사 
     */
    public boolean isNull()
    {
        return _value == null;
    }
    
    public boolean isInteger()
    {
        switch(_type)
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
                return true;
            case CHAR:  case STRING:
            {
                return _value.toString().matches("[+-]?\\d+");
            }
        }
        return false;
    }
    
    public boolean isReal()
    {
        switch(_type)
        {
            case FLOAT: case DOUBLE:
                return true;
            case CHAR:  case STRING:
            {
                return _value.toString().matches("[+-]?\\d+(\\.\\d+)");
            }
        }
        return false;
    }
    /*
     * 현재 등록된 값의 데이터 유형을 반환한다.
     */
    public ValueType getType()
    {
        return _type;
    }
    
    public Class<?> getTypeClass()
        throws ClassNotFoundException
    {
        return Class.forName(_type.getClassName());
    }

    /*
     * 지원가능한 데이터 유형을 검사한다.
     */
    private Object assertType(Object value)
        throws Exception
    {
        if (value != null)
        {
            // 지원가능한 형식 검사
            Class<? extends Object> cls = value.getClass();
            String clsName = cls.getName();

            if (!_types.containsKey(clsName))
                throw new Exception("지원하지 않는 형식입니다.");
        }

        return value;
    }

    /* 
     * 두 피연산자의 데이터 타입을 비교하여 적합한 데이터 유형을 선택한다.
     */
//    private Class<?> getBiggerType(Value left, Value right)
//        throws Exception
//    {
//        if (left == null && right == null)
//            return null;
//
//        Class<?> clsLeft  = left.getTypeClass();
//        Class<?> clsRight = right.getTypeClass();
//        
//        ValueType tpLeft = left.getType();
//        ValueType tpRight= right.getType();
//
//        return tpLeft.isBigger(tpRight)?clsLeft:clsRight;
//    }

    public boolean toBool()
        throws Exception
    {
        try
        {
            switch(_type)
            {
                case BOOLEAN:
                    return (Boolean )_value;
                case BYTE:
                    return ((Byte   )_value)!=0?true:false;
                case SHORT:
                    return ((Short  )_value)!=0?true:false;
                case INTEGER:
                    return ((Integer)_value)!=0?true:false;
                case LONG:
                    return ((Long   )_value)!=0?true:false;
                case FLOAT:
                    return ((Float  )_value)!=0?true:false;
                case DOUBLE:
                    return ((Double )_value)!=0?true:false;
                case CHAR:  case STRING  :
                    return Boolean.valueOf(_value.toString());
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "인식할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
    }

    public int toInteger()
        throws Exception
    {
        try
        {
            switch(_type)
            {
                case BOOLEAN:
                    return ((Boolean)_value)?1:0;
                case BYTE:
                    return ((Byte   )_value).intValue();
                case SHORT:
                    return ((Short  )_value).intValue();
                case INTEGER:
                    return ((Integer)_value);
                case LONG:
                    return ((Long   )_value).intValue();
                case FLOAT:
                    return ((Float  )_value).intValue();
                case DOUBLE:
                    return ((Double )_value).intValue();
                case CHAR:  case STRING:
                {
                    if (isInteger() || isReal())
                        return Integer.parseInt(_value.toString());
                    else
                        return 0;
                }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "인식할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
    }

    public long toLong()
        throws Exception
    {
        try
        {
            switch(_type)
            {
                case BOOLEAN: 
                    return ((Boolean)_value)?1:0;
                case BYTE:
                    return ((Byte   )_value).longValue();
                case SHORT:
                    return ((Short  )_value).longValue();
                case INTEGER:
                    return ((Integer)_value).longValue();
                case LONG:
                    return ((Long   )_value);
                case FLOAT:
                    return ((Float  )_value).longValue();
                case DOUBLE:
                    return ((Double )_value).longValue();
                case CHAR:  case STRING:
                {
                    if (isInteger() || isReal())
                        return Long.parseLong(_value.toString());
                    else
                        return 0;
                }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "인식할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
    }

    public double toDouble()
        throws Exception
    {
        try
        {
            switch(_type)
            {
                case BOOLEAN: 
                    return ((Boolean)_value)?1:0;
                case BYTE:
                    return ((Byte   )_value).doubleValue();
                case SHORT:
                    return ((Short  )_value).doubleValue();
                case INTEGER:
                    return ((Integer)_value).doubleValue();
                case LONG:
                    return ((Long   )_value).doubleValue();
                case FLOAT:
                    return ((Float  )_value).doubleValue();
                case DOUBLE:
                    return ((Double )_value).doubleValue();
                case CHAR:  case STRING:
                {
                    if (isInteger() || isReal())
                        return Double.parseDouble(_value.toString());
                    else
                        return 0;
                }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "인식할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
    }

    public String toString()
    {
        return (_value==null?"":_value.toString());
    }

    /*
     * 두 피연산자의 합을 구한다.
     */
    public Value add(Value value)
        throws Exception
    {
        Value newVal = null;
        
        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .add((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .add((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .add((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.add((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.add((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value add(boolean value) throws Exception { return add(new Value(value)); }
    public Value add(byte    value) throws Exception { return add(new Value(value)); }
    public Value add(short   value) throws Exception { return add(new Value(value)); }
    public Value add(int     value) throws Exception { return add(new Value(value)); }
    public Value add(long    value) throws Exception { return add(new Value(value)); }
    public Value add(float   value) throws Exception { return add(new Value(value)); }
    public Value add(double  value) throws Exception { return add(new Value(value)); }
    public Value add(char    value) throws Exception { return add(new Value(value)); }
    public Value add(String  value) throws Exception { return add(new Value(value)); }


    /*
     * 두 피연산자의 차를 구한다.
     */
    public Value substract(Value value)
        throws Exception
    {
        Value newVal = null;
        
        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .substract((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .substract((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .substract((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.substract((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.substract((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value substract(boolean value) throws Exception { return substract(new Value(value)); }
    public Value substract(byte    value) throws Exception { return substract(new Value(value)); }
    public Value substract(short   value) throws Exception { return substract(new Value(value)); }
    public Value substract(int     value) throws Exception { return substract(new Value(value)); }
    public Value substract(long    value) throws Exception { return substract(new Value(value)); }
    public Value substract(float   value) throws Exception { return substract(new Value(value)); }
    public Value substract(double  value) throws Exception { return substract(new Value(value)); }
    public Value substract(char    value) throws Exception { return substract(new Value(value)); }
    public Value substract(String  value) throws Exception { return substract(new Value(value)); }


    /*
     * 두 피연산자의 곱을 구한다.
     */
    public Value multiply(Value value)
        throws Exception
    {
        Value newVal = null;
        
        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .multiply((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .multiply((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .multiply((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.multiply((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.multiply((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value multiply(boolean value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(byte    value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(short   value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(int     value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(long    value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(float   value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(double  value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(char    value) throws Exception { return multiply(new Value(value)); }
    public Value multiply(String  value) throws Exception { return multiply(new Value(value)); }

    /*
     * 두 피연산자의 나눈값을 구한다.
     */
    public Value divide(Value value)
        throws Exception
    {
        Value newVal = null;
        
        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .divide((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .divide((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .divide((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.divide((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.divide((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value divide(boolean value) throws Exception { return divide(new Value(value)); }
    public Value divide(byte    value) throws Exception { return divide(new Value(value)); }
    public Value divide(short   value) throws Exception { return divide(new Value(value)); }
    public Value divide(int     value) throws Exception { return divide(new Value(value)); }
    public Value divide(long    value) throws Exception { return divide(new Value(value)); }
    public Value divide(float   value) throws Exception { return divide(new Value(value)); }
    public Value divide(double  value) throws Exception { return divide(new Value(value)); }
    public Value divide(char    value) throws Exception { return divide(new Value(value)); }
    public Value divide(String  value) throws Exception { return divide(new Value(value)); }    

    /*
     * 두 피연산자의 나눈 나머지값을 구한다.
     */
    public Value modular(Value value)
        throws Exception
    {
        Value newVal = null;
        
        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .modular((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .modular((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .modular((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.modular((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.modular((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value modular(boolean value) throws Exception { return modular(new Value(value)); }
    public Value modular(byte    value) throws Exception { return modular(new Value(value)); }
    public Value modular(short   value) throws Exception { return modular(new Value(value)); }
    public Value modular(int     value) throws Exception { return modular(new Value(value)); }
    public Value modular(long    value) throws Exception { return modular(new Value(value)); }
    public Value modular(float   value) throws Exception { return modular(new Value(value)); }
    public Value modular(double  value) throws Exception { return modular(new Value(value)); }
    public Value modular(char    value) throws Exception { return modular(new Value(value)); }
    public Value modular(String  value) throws Exception { return modular(new Value(value)); }
    

    /*
     * 두 피연산자 같은 값인지 판별한다.
     */
    public Value equal(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .equal((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .equal((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .equal((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.equal((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.equal((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value equal(boolean value) throws Exception { return equal(new Value(value)); }
    public Value equal(byte    value) throws Exception { return equal(new Value(value)); }
    public Value equal(short   value) throws Exception { return equal(new Value(value)); }
    public Value equal(int     value) throws Exception { return equal(new Value(value)); }
    public Value equal(long    value) throws Exception { return equal(new Value(value)); }
    public Value equal(float   value) throws Exception { return equal(new Value(value)); }
    public Value equal(double  value) throws Exception { return equal(new Value(value)); }
    public Value equal(char    value) throws Exception { return equal(new Value(value)); }
    public Value equal(String  value) throws Exception { return equal(new Value(value)); }
 
    /*
     * 두 피연산자 다른 값인지 판별한다.
     */
    public Value notEqual(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .notEqual((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .notEqual((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .notEqual((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.notEqual((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.notEqual((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value notEqual(boolean value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(byte    value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(short   value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(int     value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(long    value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(float   value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(double  value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(char    value) throws Exception { return notEqual(new Value(value)); }
    public Value notEqual(String  value) throws Exception { return notEqual(new Value(value)); }    

    /*
     * 두 피연산자의 크기를 비교한다.
     */
    public Value grateEqual(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .grateEqual((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .grateEqual((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .grateEqual((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.grateEqual((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.grateEqual((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value grateEqual(boolean value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(byte    value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(short   value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(int     value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(long    value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(float   value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(double  value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(char    value) throws Exception { return grateEqual(new Value(value)); }
    public Value grateEqual(String  value) throws Exception { return grateEqual(new Value(value)); }    

    /*
     * 두 피연산자의 크기를 비교한다.
     */
    public Value lessEqual(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .lessEqual((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .lessEqual((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .lessEqual((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.lessEqual((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.lessEqual((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value lessEqual(boolean value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(byte    value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(short   value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(int     value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(long    value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(float   value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(double  value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(char    value) throws Exception { return lessEqual(new Value(value)); }
    public Value lessEqual(String  value) throws Exception { return lessEqual(new Value(value)); }    

    /*
     * 두 피연산자의 크기를 비교한다.
     */
    public Value grate(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .grate((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .grate((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .grate((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.grate((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.grate((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value grate(boolean value) throws Exception { return grate(new Value(value)); }
    public Value grate(byte    value) throws Exception { return grate(new Value(value)); }
    public Value grate(short   value) throws Exception { return grate(new Value(value)); }
    public Value grate(int     value) throws Exception { return grate(new Value(value)); }
    public Value grate(long    value) throws Exception { return grate(new Value(value)); }
    public Value grate(float   value) throws Exception { return grate(new Value(value)); }
    public Value grate(double  value) throws Exception { return grate(new Value(value)); }
    public Value grate(char    value) throws Exception { return grate(new Value(value)); }
    public Value grate(String  value) throws Exception { return grate(new Value(value)); }    

    /*
     * 두 피연산자의 크기를 비교한다.
     */
    public Value less(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .less((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .less((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .less((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.less((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.less((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value less(boolean value) throws Exception { return less(new Value(value)); }
    public Value less(byte    value) throws Exception { return less(new Value(value)); }
    public Value less(short   value) throws Exception { return less(new Value(value)); }
    public Value less(int     value) throws Exception { return less(new Value(value)); }
    public Value less(long    value) throws Exception { return less(new Value(value)); }
    public Value less(float   value) throws Exception { return less(new Value(value)); }
    public Value less(double  value) throws Exception { return less(new Value(value)); }
    public Value less(char    value) throws Exception { return less(new Value(value)); }
    public Value less(String  value) throws Exception { return less(new Value(value)); }    

    /*
     * 두 피연산자의 논리합을 연산한다.
     */
    public Value or(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .or((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .or((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .or((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.or((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.or((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value or(boolean value) throws Exception { return or(new Value(value)); }
    public Value or(byte    value) throws Exception { return or(new Value(value)); }
    public Value or(short   value) throws Exception { return or(new Value(value)); }
    public Value or(int     value) throws Exception { return or(new Value(value)); }
    public Value or(long    value) throws Exception { return or(new Value(value)); }
    public Value or(float   value) throws Exception { return or(new Value(value)); }
    public Value or(double  value) throws Exception { return or(new Value(value)); }
    public Value or(char    value) throws Exception { return or(new Value(value)); }
    public Value or(String  value) throws Exception { return or(new Value(value)); }

    /*
     * 두 피연산자의 논리곱을 연산한다.
     */
    public Value and(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .and((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .and((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .and((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.and((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.and((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value and(boolean value) throws Exception { return and(new Value(value)); }
    public Value and(byte    value) throws Exception { return and(new Value(value)); }
    public Value and(short   value) throws Exception { return and(new Value(value)); }
    public Value and(int     value) throws Exception { return and(new Value(value)); }
    public Value and(long    value) throws Exception { return and(new Value(value)); }
    public Value and(float   value) throws Exception { return and(new Value(value)); }
    public Value and(double  value) throws Exception { return and(new Value(value)); }
    public Value and(char    value) throws Exception { return and(new Value(value)); }
    public Value and(String  value) throws Exception { return and(new Value(value)); }

    /*
     * 피연산자의 반대값을 연산한다.
     */
    public Value not()
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .not((Boolean)_value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .not((Integer)_value); break; }
                case LONG    : { newVal = VarLong  .not((Long   )_value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.not((Double )_value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.not((String )_value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }

    /*
     * 두 피연산자의 비트 OR 연산한다.
     */
    public Value bitOr(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .bitOr((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .bitOr((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .bitOr((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.bitOr((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.bitOr((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value bitOr(boolean value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(byte    value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(short   value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(int     value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(long    value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(float   value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(double  value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(char    value) throws Exception { return bitOr(new Value(value)); }
    public Value bitOr(String  value) throws Exception { return bitOr(new Value(value)); }

    /*
     * 두 피연산자의 비트 AND 연산한다.
     */
    public Value bitAnd(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .bitAnd((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .bitAnd((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .bitAnd((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.bitAnd((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.bitAnd((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value bitAnd(boolean value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(byte    value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(short   value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(int     value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(long    value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(float   value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(double  value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(char    value) throws Exception { return bitAnd(new Value(value)); }
    public Value bitAnd(String  value) throws Exception { return bitAnd(new Value(value)); }

    /*
     * 두 피연산자의 비트 XOR 연산한다.
     */
    public Value bitXor(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .bitXor((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .bitXor((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .bitXor((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.bitXor((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.bitXor((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value bitXor(boolean value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(byte    value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(short   value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(int     value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(long    value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(float   value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(double  value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(char    value) throws Exception { return bitXor(new Value(value)); }
    public Value bitXor(String  value) throws Exception { return bitXor(new Value(value)); }

    /*
     * 피연산자의 비트 NOT 연산한다.
     */
    public Value bitNot()
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .bitNot((Boolean)_value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .bitNot((Integer)_value); break; }
                case LONG    : { newVal = VarLong  .bitNot((Long   )_value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.bitNot((Double )_value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.bitNot((String )_value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }

    /*
     * 피연산자의 값을 지정한 수 만큼 좌로 이동한다.
     */
    public Value leftShift(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .leftShift((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .leftShift((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .leftShift((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.leftShift((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.leftShift((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value leftShift(boolean value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(byte    value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(short   value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(int     value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(long    value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(float   value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(double  value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(char    value) throws Exception { return leftShift(new Value(value)); }
    public Value leftShift(String  value) throws Exception { return leftShift(new Value(value)); }
    
    /*
     * 피연산자의 값을 지정한 수 만큼 우로 이동한다.
     */
    public Value rightShift(Value value)
        throws Exception
    {
        Value newVal = null;

        try
        {
            switch(_type)
            {
                case BOOLEAN : { newVal = VarBool  .rightShift((Boolean)_value, value); break; }
                case BYTE    : case SHORT   :
                case INTEGER : { newVal = VarInt   .rightShift((Integer)_value, value); break; }
                case LONG    : { newVal = VarLong  .rightShift((Long   )_value, value); break; }
                case FLOAT   : 
                case DOUBLE  : { newVal = VarDouble.rightShift((Double )_value, value); break; }
                case CHAR    : 
                case STRING  : { newVal = VarString.rightShift((String )_value, value); break; }
                default:
                {
                    Class<?> cls = _value.getClass();
                    throw new Exception
                    (
                        "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                    );
                }
            }
        }
        catch(Exception ex){ throw ex; }
        
        return newVal;
    }
    public Value rightShift(boolean value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(byte    value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(short   value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(int     value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(long    value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(float   value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(double  value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(char    value) throws Exception { return rightShift(new Value(value)); }
    public Value rightShift(String  value) throws Exception { return rightShift(new Value(value)); }
}
