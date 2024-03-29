package planz.util;

import java.util.*;

/**
 * Cuckoo Hash Map
 *
 * 일단 졸~ 빠르다고는 한다. 아직 테스트는 해보지 않았는데...
 * 해시값을 구하는 방식이 좀 다른듯... 좀 더 명확한 정보를 찾아봐야겠지만,
 * HashMap의 성능을 뛰어 넘는다고 한다.
 * 
 * 졸라 테스트 해봐봐봐봐~
 * 근데. 말위지 멀티스레딩 기반에서는 딱히 성능을 발휘하지는 못하는 것 같음.
 * 버킷-레벨 락 방법으로 구현되지만, 두 곳 이상의 메모리 주소를 원자적으로
 * 변경해야 하기 때문에 현재의 컴퓨터 아키텍처에서는 락 프리 기반의 구현이
 *  가능하지 않다고 알려져 있다고 하네... ㅡㅡ^
 * 
 * 쿠쿠 해싱은 한 개의 해시 테이블을 사용하는 것이 아니라 두 개 이상의 
 * 해시 테이블을 사용하는 것이 특징인 최신 해싱 방법이다.. 그러나 두개 이상의 
 * 해시 테이블을 사용하는 만큼 메모리도 많이 잡아먹는듯하다.
 * 일단 성능이 기존 HashMap보다 월등히 좋지 않다면, 걍 HashMap을 쓰도록~!!!
 * 
 * Implementation of Map interface using Cuckoo Hashing.
 * Using two hash functions instead of single hash function to determine the
 * possible location of an entry in the map. An entry is allocated in one of the
 * two possible location of for the key, kicking out any previous entry from the
 * position if present. The key thrown out now imitates behavior of the new
 * entry and tries to accomodate itself in one of the two possible locations.
 * This process may result in an infinite loop, in which case, the underlying
 * data structure is re-hashed and all the keys from the old data structure is
 * re-mapped to their preferred locations in the new data structure.
 * 
 * In this simplified implementation, we take a array data structure, each key
 * value pair represented by an Entry<K, V>. Two hash functions hash1 and hash2
 * are used to determine index position to insert an entry to table.
 * 
 */

public class CHashMap<K, V> extends AbstractMap<K, V> implements
        Map<K, V>
{
    static final int   DEFAULT_INITIAL_CAPACITY = 16;
    static final int   MAXIMUM_CAPACITY         = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR      = 0.75f;

    /* Test main */
    public static void main(String[] args)
    {
        Map<Integer, String> map = new CHashMap<Integer, String>();
        int itemCount = 25;
        for (int i = 0; i < itemCount; i++)
        {
            Integer key = i;
            String val = "Value_" + i;
            map.put(key, val);
        }

        System.out.println(map.get(1));

        for (String v : map.values())
        {
            System.out.println(v);
        }
    }

    static class DefaultHashFunction<T> implements HashFunction<T>
    {
        private static final Random ENGINE = new Random();
        private int rounds;

        public DefaultHashFunction()
        {
            this(1);
        }

        public DefaultHashFunction(int rounds)
        {
            this.rounds = rounds;
        }

        public int hash(Object key, int limit)
        {
            ENGINE.setSeed(key.hashCode());
            int h = ENGINE.nextInt(limit);

            for (int i = 1; i < this.rounds; i++)
                h = ENGINE.nextInt(limit);

            return h;
        }
    }

    static class Entry<K, V> implements Map.Entry<K, V>
    {
        final K key;
        V value;

        Entry(K k, V v)
        {
            value = v;
            key = k;
        }

        @SuppressWarnings("rawtypes")
        public final boolean equals(Object o)
        {
            if (!(o instanceof Map.Entry))
                return false;

            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2)))
            {
                Object v1 = getValue();
                Object v2 = e.getValue();

                if (v1 == v2 || (v1 != null && v1.equals(v2)))
                    return true;
            }
            return false;
        }

        public final K getKey()
        {
            return CHashMap.unmaskNull(key);
        }

        public final V getValue()
        {
            return value;
        }

        public final int hashCode()
        {
            return (key == null ? 0 : key.hashCode())
                    ^ (value == null ? 0 : value.hashCode());
        }

        public final V setValue(V newValue)
        {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final String toString()
        {
            return getKey() + "=>" + getValue();
        }
    }

    static interface HashFunction<T>
    {
        public int hash(Object key, int limit);
    }

    static <T> T maskNull(T key)
    {
        return key == null ? (T) NULL_KEY : key;
    }

    static <T> T unmaskNull(T key)
    {
        return (key == NULL_KEY ? null : key);
    }

    transient Entry<K, V>[] table;

    transient int size;

    int threshold;

    final float loadFactor;

    final transient HashFunction<K> hash1;

    final transient HashFunction<K> hash2;

    static final Object NULL_KEY = new Object();

    public CHashMap()
    {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        hash1 = new DefaultHashFunction<K>(2);
        hash2 = new DefaultHashFunction<K>(3);
        init();
    }

    public CHashMap(HashFunction<K> h1, HashFunction<K> h2)
    {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, h1, h2);
    }

    public CHashMap(int initialCapacity)
    {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public CHashMap(int initialCapacity, float loadFactor)
    {
        this
        (
            initialCapacity, loadFactor
          , new DefaultHashFunction<K>(2)
          , new DefaultHashFunction<K>(3)
        );
    }

    public CHashMap(int initialCapacity, float loadFactor, HashFunction<K> h1, HashFunction<K> h2)
    {
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;

        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
        table = new Entry[capacity];
        hash1 = h1;
        hash2 = h2;
        init();
    }

    public CHashMap(Map<? extends K, ? extends V> m)
    {
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
                DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
    }

    int capacity()
    {
        return table.length;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        Set<java.util.Map.Entry<K, V>> es = new HashSet<java.util.Map.Entry<K, V>>();
        for (Entry<K, V> e : table)
        {
            if (e != null)
                es.add(e);
        }

        return es;
    }

    public V get(Object key)
    {
        Object k = maskNull(key);

        int hash = hash(hash1, k);
        Object k2;
        Entry<K, V> e = table[hash];
        if (e != null && ((k2 = e.key) == k || k.equals(k2)))
            return e.value;

        hash = hash(hash2, k);
        e = table[hash];
        if (e != null && ((k2 = e.key) == k || k.equals(k2)))
            return e.value;

        return null;
    }

    private int hash(HashFunction<K> func, Object key)
    {
        return func.hash(key, table.length);
    }

    private void init() { }

    private boolean insertEntry(Entry<K, V> e)
    {
        int count = 0;
        Entry<K, V> current = e;
        int index = hash(hash1, current.key);

        while (current != e || count < table.length)
        {
            Entry<K, V> temp = table[index];
            if (temp == null)
            {
                table[index] = current;
                return true;
            }

            table[index] = current;
            current = temp;

            if (index == hash(hash1, current.key))
                index = hash(hash2, current.key);
            else
                index = hash(hash1, current.key);

            ++count;
        }

        return false;
    }

    public boolean isEmpty()
    {
        return size == 0;
    }

    float loadFactor()
    {
        return loadFactor;
    }

    public V put(K key, V value)
    {
        return put(key, value, false);
    }

    private V put(K key, V value, boolean isRehash)
    {
        Object k = maskNull(key);

        if (containsKey(k))
            return null;

        if (insertEntry(new Entry<K, V>((K) k, value)))
        {
            if (!isRehash)
                size++;

            return null;
        }

        rehash(2 * table.length);
        return put((K) k, value);
    }

    private void rehash(int newCapacity)
    {
        Entry<K, V>[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity >= MAXIMUM_CAPACITY)
        {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry<K, V>[] newTable = new Entry[newCapacity];
        table = newTable;
        for (Entry<K, V> e : oldTable)
        {
            if (e != null)
                put(e.key, e.value, true);
        }

        threshold = (int) (newCapacity * loadFactor);
    }

    public int size()
    {
        return size;
    }

}