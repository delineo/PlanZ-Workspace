package planz.core.variant;

public final class VarString
{
    public static Value add(String left, Value right)
        throws Exception
    {

        StringBuffer sb = new StringBuffer();
        sb.append(left);
        sb.append(right.toString());
        Value value = new Value(sb.toString());
        sb.setLength(0);
        sb = null;
        
        return value;
    }

    public static Value substract(String left, Value right)
        throws Exception
    {
        return new Value(left);
    }

    public static Value multiply(String left, Value right)
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
                StringBuffer sb = new StringBuffer();

                long nCnt = right.toLong();
                for (long loop=0; loop<nCnt;nCnt++)
                    sb.append(left);

                value = new Value(sb);
                sb.setLength(0);
                sb = null;
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

    public static Value divide(String left, Value right)
        throws Exception
    {
        return new Value(left);
    }

    public static Value modular(String left, Value right)
        throws Exception
    {
        return new Value(left);
    }

    public static Value equal(String left, Value right)
        throws Exception
    {
        return new Value(left.equals(right.toString()));
    }    

    public static Value notEqual(String left, Value right)
        throws Exception
    {
        return new Value(!(left.equals(right.toString())));
    }

    public static Value grateEqual(String left, Value right)
        throws Exception
    {
        return new Value(left.compareTo(right.toString()) >= 0);
    }

    public static Value lessEqual(String left, Value right)
        throws Exception
    {
        return new Value(left.compareTo(right.toString()) <= 0);
    }

    public static Value grate(String left, Value right)
        throws Exception
    {
        return new Value(left.compareTo(right.toString()) > 0);
    }

    public static Value less(String left, Value right)
        throws Exception
    {
        return new Value(left.compareTo(right.toString()) < 0);
    }

    public static Value or(String left, Value right)
        throws Exception
    {
        return new Value
               (
                   (left == null || "".equals(left)) ? left : right.getValue()  
               );
    }

    public static Value and(String left, Value right)
        throws Exception
    {
        return new Value
               (
                   (left == null || "".equals(left)) ? right.getValue() : left 
               );
    }

    public static Value not(String right)
        throws Exception
    {
        return new Value(right == null || "".equals(right));
    }
    
    public static Value bitOr(String left, Value right)
        throws Exception
    {
        return new Value(0);
    }

    public static Value bitAnd(String left, Value right)
        throws Exception
    {
        return new Value(0);
    }

    public static Value bitXor(String left, Value right)
        throws Exception
    {
        return new Value(0);
    }

    public static Value bitNot(String right)
        throws Exception
    {
        return new Value(~0);
    }

    public static Value leftShift(String left, Value right)
        throws Exception
    {
        return new Value(0);
    }

    public static Value rightShift(String left, Value right)
        throws Exception
    {
        return new Value(0);
    }
}
