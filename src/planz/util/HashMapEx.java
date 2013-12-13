package planz.util;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;

import planz.core.parser.*;


@SuppressWarnings({"rawtypes","unchecked"})
public class HashMapEx extends ConcurrentHashMap
{
    private static final long serialVersionUID = 5615311644622944201L;

    public HashMapEx()           { super();          }
	public HashMapEx(Map map)    { this.putAll(map); }
    public HashMapEx(int factor) { super(factor);    }

/* 원래 소스 : Glue Framework에 부합되도록 컨셉을 변경한다. 
 * 기존 컨셉은 객체를 처음 추가할 경우 단순히 하나의객체로 인식(Map.put 과 같이)하여 처리하나,
 * 이후 동일한 Key에 의해 추가될 경우 배열로 변환한다.
 * 변경 컨셉은 add 함수로 추가되는 모든 객체는 개수에 상관없이 배열로 인식 할수 있도록 미리 배열을 생성하고,
 * 객체의 값을 저장한다.    
    public Object add(Object key, Object value)
    {
        Object objects= super.get(key);
        
        if (objects != null)
        {
            ArrayList list = new ArrayList();
            if (objects.getClass().isArray())
            {                
                for(Object obj : (Object[])objects)
                {
                    list.add(obj);
                }
            }
            else { list.add(objects); }

            list.add(value);

            Class cls = value.getClass();
            super.put(key, list.toArray((Object[])Array.newInstance(cls, list.size())));
        }
        else
            super.put(key, value);
        
        return super.get(key);
    }
*/
    // Glue Framework을 위한 함수
    public HashMapEx add(Object key, Object value)
    {
        // 객체를 담을 리스트 생성
        if (super.containsKey(key))
        {
	        ArrayList<Object> list = new ArrayList<Object>();

	        // 기존 객체 존재 여부검사 및 배열 저장소 정의 검사
	        Object objects= super.get(key);
	        if (objects != null)
	        {
	            if (objects.getClass().isArray())
	            {
	                for(Object obj : (Object[])objects)
	                {
	                    list.add(obj);
	                }
	            }
	            else { list.add(objects); }
	        }

	        // 신규 추가 객체 추가 
	        list.add(value);

	        Class<? extends Object> cls = (value!=null)?value.getClass():Object.class;
	        super.put(key, list.toArray((Object[])Array.newInstance(cls, list.size())));
        }
        else
            put(key, value);

        return this;
        /*
        Object[] buff = null;   // 신규 배열을 생성할 저장소 생성
        
       // 기존 객체 존재 여부검사 및 배열 저장소 정의 검사 
        Object objects = super.get(key);
        if (objects != null)
        {
            // put으로 데이터를 저장해 놓을 수 있으므로, 배열인지, 단일 인스턴스인지
            // 비교하여, 배열로 변환한다.
            if (objects.getClass().isArray())
            {
                int size = ((Object[])objects).length;
                buff = new Object[size + 1];
                
                System.arraycopy(objects, 0, buff, 0, size);
                buff[size] = value;
            }
            else
                buff = new Object[] { objects, value };
        }
        else
            buff = new Object[] { value };

        super.put(key, buff); // 신규 저장소 저장

        return buff;
        */
    }

    public HashMapEx add(Map map)
    {
        if (map == null) return this;
        
        Set keySet = map.keySet();
        for (Iterator ie = keySet.iterator(); ie.hasNext();)
        {
            Object key = ie.next();
            this.add(key, map.get(key));
        }
        return this;
    }

    public HashMapEx add(Map map, String...keys)
    {
        if (map == null) return this;
        
        for (String key : keys)
        {
            if (map.containsKey(key))
                this.add(key, map.get(key));
        }
        return this;
    }

    @Override
    public HashMapEx put(Object key, Object value)
    {
        super.put(key, value);
        return this;
    }

    public HashMapEx put(Map map)
    {
        if (map == null) return this;
        
        Set keySet = map.keySet();
        for (Iterator ie = keySet.iterator(); ie.hasNext();)
        {
            Object key = ie.next();
            this.put(key, map.get(key));
        }
        return this;
    }

    public HashMapEx put(Map map, String...keys)
    {
        if (map == null) return this;
        
        for (String key : keys)
        {
            if (map.containsKey(key))
                this.put(key, map.get(key));
        }
        return this;
    }

    public Object[] gets(Object key)
    {
        Object objects= super.get(key);
        
        if (objects != null)
        {
            if (objects.getClass().isArray())
                return (Object[])objects;
            
            return new Object[] { objects }; 
        }

        return new Object[] {};
    }

    public String getString(Object key)
    {
        Object val = super.get(key);

        if (val == null) return "";
        return String.valueOf(val);
    }
    
    public String getString(Object key, String defVal)
    {
        Object val = super.get(key);

        if (val == null) return defVal;
        return String.valueOf(val);
    }

    public Long getLong(Object key)
    {
        Object val = super.get(key);
        if (val == null) return 0L;
        if ("".equals(val.toString())) return 0L;
        return Long.parseLong(val.toString());
    }

    public Long getLong(Object key, Long defVal)
    {
        Object val = super.get(key);
        if (val == null) return defVal;
        if ("".equals(val.toString())) return 0L;
        return Long.parseLong(val.toString());
    }

    public Integer getInt(Object key)
    {
        Object val = super.get(key);
        if (val == null) return 0;
        if ("".equals(val.toString())) return 0;
        return Integer.parseInt(val.toString());
    }

    public Integer getInt(Object key, Integer defVal)
    {
        Object val = super.get(key);
        if (val == null) return defVal;
        if ("".equals(val.toString())) return 0;
        return Integer.parseInt(val.toString());
    }

    public Float getFloat(Object key)
    {
        Object val = super.get(key);
        if (val == null) return 0f;
        if ("".equals(val.toString())) return 0f;
        return Float.parseFloat(val.toString());
    }

    public Float getFloat(Object key, Float defVal)
    {
        Object val = super.get(key);
        if (val == null) return defVal;
        if ("".equals(val.toString())) return 0f;
        return Float.parseFloat(val.toString());
    }    
    
    public Double getDouble(Object key)
    {
        Object val = super.get(key);
        if (val == null) return 0d;
        if ("".equals(val.toString())) return 0d;
        return Double.parseDouble(val.toString());
    }

    public Double getDouble(Object key, Double defVal)
    {
        Object val = super.get(key);
        if (val == null) return defVal;
        if ("".equals(val.toString())) return 0d;
        return Double.parseDouble(val.toString());
    }    

    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();

        Set keySet = keySet();
        for (Iterator ie = keySet.iterator(); ie.hasNext();)
        {
            Object key = ie.next();
            Object val = get(key);
            
            if (val != null && val.getClass().isArray())
            {
                sb.append(String.format("%-25s : [ ", String.valueOf(key) ));

                StringBuilder ssb = new StringBuilder();
                Object[] lst = (Object[])val;

                for (int i =0; i < lst.length; i++)
                {
                    if (ssb.length() > 0) ssb.append(", ");
                    ssb.append(String.valueOf(lst[i]));
                }
                
                sb.append(ssb.toString()+" ]\n");
            }
            else
            {
                sb.append(String.format
                (
                    "%-25s : %s\n"
                  , String.valueOf(key)
                  , String.valueOf(val)
                ));
            }
        }

        return sb.toString();        
    }
    
    public String toJson()
    {
        return Json.FromMap(this);
    }

    public class Run {}
    public void each(Run run)
    {
        try
        {
            Class<?> clsRun = run.getClass();
            Method mtdRun = clsRun.getMethod("run", Object.class, Object.class);
            if (mtdRun == null) return;

            if (!mtdRun.isAccessible()) mtdRun.setAccessible(true);
            
            Set<?> keys = this.keySet();
            
            for (Iterator<?> itor = keys.iterator(); itor.hasNext();)
            {
                Object key = itor.next();            
                Object val = this.get(key);
                
                Object isContinue = mtdRun.invoke(run, key, val);
                if (isContinue.equals(false))
                    break;
            }
        }
        catch(Exception ex) {}
    }
}
