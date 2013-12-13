package planz.core.variant;

public final class VarDouble
{
    public static Value add(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:
            case BYTE:  case SHORT: case INTEGER:   case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left + right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                StringBuffer sb = new StringBuffer();
                
                sb.append(String.valueOf(left));
                sb.append(right.getValue());
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

    public static Value substract(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:
            case BYTE:  case SHORT: case INTEGER:   case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left - right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                StringBuffer sb = new StringBuffer();
                
                sb.append(String.valueOf(left));
                sb.append(right.getValue());
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

    public static Value multiply(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:
            case BYTE:  case SHORT: case INTEGER:   case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left * right.toDouble()
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

    public static Value divide(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:
            case BYTE:  case SHORT: case INTEGER:   case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left / right.toDouble()
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

    public static Value modular(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:
            case BYTE:  case SHORT: case INTEGER:   case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left % right.toDouble()
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

    public static Value equal(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left == right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left == right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        left == right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf(left).equals(right.toString())
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

    public static Value notEqual(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left != right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left != right.toInteger()
                    );
                else if (right.isReal())
                    value = new Value
                    (
                        left != right.toDouble()
                    );
                else
                    value = new Value
                    (
                        !(String.valueOf(left).equals(right.toString()))
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

    public static Value grateEqual(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left >= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger() || right.isReal())
                    value = new Value
                    (
                        left >= right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left)
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

    public static Value lessEqual(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left <= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger() || right.isReal())
                    value = new Value
                    (
                        left <= right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left)
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

    public static Value grate(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left > right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger() || right.isReal())
                    value = new Value
                    (
                        left > right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left)
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

    public static Value less(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN : 
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    left > right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger() || right.isReal())
                    value = new Value
                    (
                        left < right.toDouble()
                    );
                else
                    value = new Value
                    (
                        String.valueOf  (left)
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

    public static Value or(double left, Value right)
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
                    left == 0 ? left : right.getValue()
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

    public static Value and(double left, Value right)
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
                    left == 0 ? right.getValue() : left 
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

    public static Value not(double right)
        throws Exception
    {
        return new Value(right==0);
    }

    public static Value bitOr(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (long)left | right.toLong() 
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

    public static Value bitAnd(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (long)left & right.toLong() 
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

    public static Value bitXor(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (long)left ^ right.toLong() 
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

    public static Value bitNot(double right)
        throws Exception
    {
        return new Value(~((long)right));
    }

    public static Value leftShift(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (long)left << right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( (long)left );
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

    public static Value rightShift(double left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
            case BOOLEAN :
            case BYTE:  case SHORT: case INTEGER:
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (long)left >> right.toLong() 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( (long)left );
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
