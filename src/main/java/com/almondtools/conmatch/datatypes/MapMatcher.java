package com.almondtools.conmatch.datatypes;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsNull;

public class MapMatcher<K,V> extends TypeSafeMatcher<Map<K,V>>{
	
	private Class<K> key;
	private Class<V> value;
	private Map<Matcher<K>,Matcher<V>> entries;
	
	public MapMatcher(Class<K> key, Class<V> value) {
		this.key = key;
		this.value = value;
		this.entries = new LinkedHashMap<>();
	}

	public MapMatcher<K,V> entry(K key, V value) {
		return entry(match(key, this.key), match(value, this.value));
	}

	public MapMatcher<K,V> entry(Matcher<K> key, V value) {
		return entry(key, match(value, this.value));
	}

	public MapMatcher<K,V> entry(K key, Matcher<V> value) {
		return entry(match(key, this.key), value);
	}

	public MapMatcher<K,V> entry(Matcher<K> key, Matcher<V> value) {
		entries.put(key, value);
		return this;
	}
	
	private <T> Matcher<T> match(T element, Class<T> clazz) {
		if (element == null) {
			return nullValue(clazz);
		} else {
			return equalTo(element);
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(entries);
	}
	
	@Override
	protected void describeMismatchSafely(Map<K, V> item, Description mismatchDescription) {
		List<Entry<Matcher<K>, Matcher<V>>> unmatched = new LinkedList<>(entries.entrySet());
		List<Entry<Matcher<K>, Matcher<V>>> matched = new LinkedList<>();
		List<Entry<K, V>> notfound = new LinkedList<>();
		
		for (Entry<K, V> entry : item.entrySet()) {
		
			boolean success = tryMatch(unmatched, matched, entry);
			if (!success) {
				notfound.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
			}
		}
		
		if (!unmatched.isEmpty()) {
			mismatchDescription.appendText("missing entries ").appendValue(map(unmatched));
		}
		if (!unmatched.isEmpty() && !notfound.isEmpty()) {
			mismatchDescription.appendText(", ");
		}
		if (!notfound.isEmpty()) {
			mismatchDescription.appendText("unmatched entries ").appendValue(mapValues(notfound));
		}
	}

	private Map<Matcher<K>, Matcher<V>> map(List<Entry<Matcher<K>, Matcher<V>>> entries) {
		Map<Matcher<K>, Matcher<V>> map = new HashMap<>();
		for (Entry<Matcher<K>, Matcher<V>> entry : entries) {
			map.put(entry.getKey(), entry.getValue());
		}
		return map;
	}

	private Map<String, String> mapValues(List<Entry<K, V>> entries) {
		Matcher<K> keyMatcher = bestKeyMatcher();
		Matcher<V> valueMatcher = bestValueMatcher();
		Map<String, String> map = new HashMap<>();
		for (Entry<K, V> entry : entries) {
			String key = descriptionOf(keyMatcher, entry.getKey());
			String value = descriptionOf(valueMatcher, entry.getValue());
			map.put(key, value);
		}
		return map;
	}

	private Matcher<V> bestValueMatcher() {
		for (Matcher<V> matcher : entries.values()) {
			if (matcher.getClass() != IsNull.class) {
				return matcher;
			}
		}
		return equalTo(null);
	}

	private Matcher<K> bestKeyMatcher() {
		for (Matcher<K> matcher : entries.keySet()) {
			if (matcher.getClass() != IsNull.class) {
				return matcher;
			}
		}
		return equalTo(null);
	}

	private <T> String descriptionOf(Matcher<T> valueMatcher, T value) {
		StringDescription description = new StringDescription();
		valueMatcher.describeMismatch(value, description);
		return description.toString();
	}

	@Override
	protected boolean matchesSafely(Map<K, V> item) {
		List<Entry<Matcher<K>, Matcher<V>>> unmatched = new LinkedList<>(entries.entrySet());
		List<Entry<Matcher<K>, Matcher<V>>> matched = new LinkedList<>();
		
		for (Entry<K, V> entry : item.entrySet()) {
		
			boolean success = tryMatch(unmatched, matched, entry);
			if (!success) {
				return false;
			}
		}
		
		return unmatched.isEmpty();
	}

	private boolean tryMatch(List<Entry<Matcher<K>, Matcher<V>>> unmatched, List<Entry<Matcher<K>, Matcher<V>>> matched, Entry<K, V> entry) {
		K key = entry.getKey();
		V value = entry.getValue();

		Iterator<Entry<Matcher<K>, Matcher<V>>> matchers = unmatched.iterator();
		while (matchers.hasNext()) {
			Entry<Matcher<K>, Matcher<V>> matcher = matchers.next();
			if (matcher.getKey().matches(key) && matcher.getValue().matches(value)) {
				matchers.remove();
				matched.add(matcher);
				return true;
			}
		}
		return false;
	}

	public static <K,V> MapMatcher<K, V> noEntries(Class<K> key, Class<V> value) {
		return new MapMatcher<>(key, value);
	}

	public static <K,V> MapMatcher<K, V> containsEntries(Class<K> key, Class<V> value) {
		return new MapMatcher<>(key, value);
	}

}
