package me.mastercapexd.auth.dealerships;

import java.util.Map;

public interface MapDealership<K, V> {
	Map<K, V> getMap();

	V put(K key, V value);

	V remove(K key);

	V get(Object key);

	V getOrDefault(Object key, V def);

	boolean containsKey(Object key);
}
