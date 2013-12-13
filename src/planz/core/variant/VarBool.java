package planz.core.variant;

public final class VarBool
{
    public static Value add(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) + right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) + right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) + right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                StringBuffer sb = new StringBuffer();
                
                sb.append(String.valueOf(left));
                sb.append(right.toString());
                value = new Value(sb.toString());
                sb.setLength(0);
                sb = null;
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }            
        }

        return value;
    }

    public static Value substract(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) - right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) - right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) - right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(left);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }            
        }

        return value;
    }

    public static Value multiply(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) * right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) * right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) * right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(left);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }            
        }

        return value;
    }

    public static Value divide(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) / right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) / right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) / right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(left);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }            
        }

        return value;
    }

    public static Value modular(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (int)((left?1:0) % right.toInteger())
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)((left?1:0) % right.toLong())
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) % right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(left);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value equal(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) == right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) == right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) == right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value
                (
                    String.valueOf((left?1:0)).equals(right.toString())
                );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value notEqual(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) != right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) != right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) != right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value
                (
                    !(String.valueOf((left?1:0)).equals(right.toString()))
                );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value grateEqual(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) >= right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) >= right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) >= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        (left?1:0) >= right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        (left?1:0) >= right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left?1:0)
                              .compareTo(right.toString()) >= 0
                    );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value lessEqual(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) <= right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) <= right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) <= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        (left?1:0) <= right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        (left?1:0) <= right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left?1:0)
                              .compareTo(right.toString()) <= 0
                    );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value grate(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) > right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) > right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) > right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        (left?1:0) > right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        (left?1:0) > right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left?1:0)
                              .compareTo(right.toString()) > 0
                    );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value less(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (left?1:0) < right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (left?1:0) < right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (left?1:0) < right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        (left?1:0) < right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        (left?1:0) < right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left?1:0)
                              .compareTo(right.toString()) < 0
                    );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value or(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            case CHAR:  case STRING: 
            {
                value = new Value
                (
                    left ? left : right.getValue()
                );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value and(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            case CHAR:  case STRING: 
            {
                value = new Value
                (
                    left ? right.getValue() : left 
                );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value not(boolean right)
        throws Exception
    {
        return new Value(!right);
    }

    public static Value bitOr(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left?1:0 | right.toInteger() 
                );
                break;
            }
                
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left?1:0 | right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(0);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value bitAnd(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left?1:0 & right.toInteger() 
                );
                break;
            }
                
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left?1:0 & right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(0);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value bitXor(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left?1:0 ^ right.toInteger() 
                );
                break;
            }
                
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left?1:0 ^ right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value(0);
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value bitNot(boolean right)
        throws Exception
    {
        return new Value(~(right?1:0));
    }

    public static Value leftShift(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left?1:0 << right.toInteger() 
                );
                break;
            }
                
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left?1:0 << right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( left?1:0 );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }

    public static Value rightShift(boolean left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left?1:0 >> right.toInteger() 
                );
                break;
            }
                
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left?1:0 >> right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( left?1:0 );
                break;
            }
            default:
            {
                Class<?> cls = right.getClass();
                throw new Exception
                (
                    "연산할 수 없는 데이터 형식입니다. [ " + cls.getName() + "]"
                );
            }
        }

        return value;
    }    
}
