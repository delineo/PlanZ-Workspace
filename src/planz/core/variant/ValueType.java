package planz.core.variant;

public enum ValueType
{
    NULL   ( 0, "null"               )
  , BOOLEAN( 1, "java.lang.Boolean"  )
  , BYTE   ( 2, "java.lang.Byte"     )
  , SHORT  ( 3, "java.lang.Short"    )
  , INTEGER( 4, "java.lang.Integer"  )
  , LONG   ( 5, "java.lang.Long"     )
  , FLOAT  ( 6, "java.lang.Float"    )
  , DOUBLE ( 7, "java.lang.Double"   )
  , CHAR   ( 9, "java.lang.Character")
  , STRING (10, "java.lang.String"   )
  ;

    @SuppressWarnings("unused")
    private int    _value;
    private String _className;

    private ValueType(int value, String clsName)
    {
        
        _value     = value;
        _className = clsName;
    }

    public String getClassName() { return _className; }

    public boolean isBigger(ValueType type)
    {
        return (compareTo(type)>=0);
    }
};
