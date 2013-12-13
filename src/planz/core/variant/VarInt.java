package planz.core.variant;

public final class VarInt
{
    public static Value add(int left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:   case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left + right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    left + right.toLong()
                );
                break;
            }
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

    public static Value substract(int left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:   case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left - right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    left - right.toLong()
                );
                break;
            }
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
                //sb.append(right.getValue());
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

    public static Value multiply(int left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:   case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left * right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    left * right.toLong()
                );
                break;
            }
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

    public static Value divide(int left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:   case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    left / right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    left / right.toLong()
                );
                break;
            }
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

    public static Value modular(int left, Value right)
        throws Exception
    {
        Value value = null;

        switch(right.getType())
        {
             
            case BOOLEAN:   case BYTE:  case SHORT: case INTEGER:
            {
                value = new Value
                (
                    (int)(left % right.toInteger())
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)(left % right.toLong())
                );
                break;
            }
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

    public static Value equal(int left, Value right)
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
                    left == right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left == right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left == right.toDouble()
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
    
    public static Value notEqual(int left, Value right)
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
                    left != right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left != right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left != right.toDouble()
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

    public static Value grateEqual(int left, Value right)
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
                    left >= right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left >= right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left >= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left >= right.toInteger()
                    );
                else if (right.isReal())
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

    public static Value lessEqual(int left, Value right)
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
                    left <= right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left <= right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left <= right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left <= right.toInteger()
                    );
                else if (right.isReal())
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

    public static Value grate(int left, Value right)
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
                    left > right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left > right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left > right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left > right.toInteger()
                    );
                else if (right.isReal())
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

    public static Value less(int left, Value right)
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
                    left < right.toInteger()
                );
                break;
            }
            case LONG:
            {
                value = new Value
                (
                    (long)left < right.toLong()
                );
                break;
            }
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (double)left < right.toDouble()
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                if (right.isInteger())
                    value = new Value
                    (
                        left < right.toInteger()
                    );
                else if (right.isReal())
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

    public static Value or(int left, Value right)
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

    public static Value and(int left, Value right)
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

    public static Value not(int right)
        throws Exception
    {
        return new Value(right==0);
    }

    public static Value bitOr(int left, Value right)
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
                    left | right.toInteger() 
                );
                break;
            }
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (int)(left | right.toLong()) 
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

    public static Value bitAnd(int left, Value right)
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
                    left & right.toInteger() 
                );
                break;
            }
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (int)(left & right.toLong()) 
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

    public static Value bitXor(int left, Value right)
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
                    left ^ right.toInteger() 
                );
                break;
            }
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (int)(left ^ right.toLong()) 
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

    public static Value bitNot(int right)
        throws Exception
    {
        return new Value(~right);
    }

    public static Value leftShift(int left, Value right)
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
                    left << right.toInteger() 
                );
                break;
            }
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (int)(left << right.toLong()) 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( left );
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

    public static Value rightShift(int left, Value right)
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
                    left >> right.toInteger() 
                );
                break;
            }
            case LONG:
            case FLOAT: case DOUBLE:
            {
                value = new Value
                (
                    (int)(left >> right.toLong()) 
                );
                break;
            }
            case CHAR:  case STRING: 
            {
                value = new Value( left );
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

    public static int parseHexa(String hexa)
    {
        if (hexa == null || hexa.isEmpty())
            return 0;

        hexa = hexa.replaceFirst("^0x", "");
        return Integer.parseInt(hexa, 16);
    }
}
