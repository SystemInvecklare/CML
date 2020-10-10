package com.github.systeminvecklare.cml;

import java.util.List;
import java.util.Map;

public interface IKeyValueSet<K,V> extends Iterable<IKeyValuePair<K, V>>{
	Map<K,V> asMap();
	boolean has(K key);
	List<IKeyValuePair<K, V>> getAll();
	V get(K key);
	V get(K key, V fallback);
	boolean isEmpty();
}
