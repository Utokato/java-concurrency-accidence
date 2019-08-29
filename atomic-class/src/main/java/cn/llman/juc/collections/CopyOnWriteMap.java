package cn.llman.juc.collections;

import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM2;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * custom a map with cow
 *
 * @author lma
 * @date 2019/06/13
 */
public class CopyOnWriteMap<K, V> implements Map<K, V> {

    private volatile Map<K, V> map;
    private ReentrantLock lock = new ReentrantLock();

    public CopyOnWriteMap() {
        this.init();
    }

    private void init() {
        this.map = new HashMap<>();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            HashMap<K, V> newMap = new HashMap<>(map);
            V result = newMap.put(key, value);
            this.map = newMap;
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        try {
            lock.lock();
            HashMap<K, V> newMap = new HashMap<>(map);
            V result = newMap.remove(key);
            this.map = newMap;
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try {
            lock.lock();
            HashMap<K, V> newMap = new HashMap<>(map);
            newMap.putAll(m);
            this.map = newMap;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        try {
            lock.lock();
            this.map = new HashMap<>();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
