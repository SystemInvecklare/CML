package com.github.systeminvecklare.cml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class KeyValueSet<K, V> implements IKeyValueSet<K, V> {
	private final Map<K,V> map = new LinkedHashMap<K, V>();

	@Override
	public Map<K, V> asMap() {
		return map;
	}

	@Override
	public boolean has(K key) {
		return map.containsKey(key);
	}

	@Override
	public List<IKeyValuePair<K, V>> getAll() {
		List<IKeyValuePair<K, V>> list = new ArrayList<IKeyValuePair<K,V>>();
		for(Entry<K, V> entry : map.entrySet()) {
			list.add(new KeyValuePair(entry));
		}
		return list;
	}
	
	@Override
	public Iterator<IKeyValuePair<K, V>> iterator() {
		return getAll().iterator();
	}

	@Override
	public V get(K key) {
		if(!map.containsKey(key)) {
			throw new IllegalArgumentException("No value for key "+key);
		}
		return map.get(key);
	}

	@Override
	public V get(K key, V fallback) {
		return map.containsKey(key) ? map.get(key) : fallback;
	}
	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void set(K key, V value) {
		map.put(key, value);
	}
	
	private class KeyValuePair implements IKeyValuePair<K, V> {
		private final Map.Entry<K, V> entry;
		
		public KeyValuePair(Entry<K, V> entry) {
			this.entry = entry;
		}

		@Override
		public K getName() {
			return entry.getKey();
		}

		@Override
		public V getValue() {
			return entry.getValue();
		}
	}
}
