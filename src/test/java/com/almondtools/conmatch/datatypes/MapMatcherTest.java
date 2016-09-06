package com.almondtools.conmatch.datatypes;

import static com.almondtools.conmatch.datatypes.MapMatcher.containsEntries;
import static com.almondtools.conmatch.datatypes.MapMatcher.noEntries;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class MapMatcherTest {

	@Test
	public void testMatchesSafelyWithValues() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry("foo", "bar").entry("08", "15").matchesSafely(map), is(true));
		assertThat(containsEntries(String.class, String.class).entry("08", "15").entry("foo", "bar").matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithoutValues() throws Exception {
		Map<String, String> map = map();

		assertThat(containsEntries(String.class, String.class).matchesSafely(map), is(true));
		assertThat(noEntries(String.class, String.class).matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithKeyMatcher() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry(equalTo("foo"), "bar").entry(equalTo("08"), "15").matchesSafely(map), is(true));
		assertThat(containsEntries(String.class, String.class).entry(equalTo("08"), "15").entry(equalTo("foo"), "bar").matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithValueMatcher() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry("foo", equalTo("bar")).entry("08", equalTo("15")).matchesSafely(map), is(true));
		assertThat(containsEntries(String.class, String.class).entry("08", equalTo("15")).entry("foo", equalTo("bar")).matchesSafely(map), is(true));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testMatchesSafelyWithHiddenKeyMatcher() throws Exception {
		Map<Object, String> map = (Map) map("foo", "bar", "08", "15");

		assertThat(containsEntries(Object.class, String.class).entry((Object) equalTo("foo"), "bar").entry(equalTo("08"), "15").matchesSafely(map), is(true));
		assertThat(containsEntries(Object.class, String.class).entry((Object) equalTo("08"), "15").entry(equalTo("foo"), "bar").matchesSafely(map), is(true));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testMatchesSafelyWithHiddenValueMatcher() throws Exception {
		Map<String, Object> map = (Map) map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, Object.class).entry("foo", (Object) equalTo("bar")).entry("08", equalTo("15")).matchesSafely(map), is(true));
		assertThat(containsEntries(String.class, Object.class).entry("08", (Object) equalTo("15")).entry("foo", equalTo("bar")).matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithMixedMatcher() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry(equalTo("foo"), "bar").entry("08", equalTo("15")).matchesSafely(map), is(true));
		assertThat(containsEntries(String.class, String.class).entry(equalTo("08"), "15").entry("foo", equalTo("bar")).matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithNulls() throws Exception {
		Map<String, String> map = map("foo", null, null, "15");

		assertThat(containsEntries(String.class, String.class).entry("foo", (String) null).entry((String) null, "15").matchesSafely(map), is(true));
	}

	@Test
	public void testMatchesSafelyWithValuesFails() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry("foo", "foo").entry("08", "15").matchesSafely(map), is(false));
		assertThat(containsEntries(String.class, String.class).entry("bar", "bar").entry("08", "15").matchesSafely(map), is(false));
	}

	@Test
	public void testMatchesSafelyWithoutValuesFails() throws Exception {
		Map<String, String> map = map("foo", "bar");

		assertThat(containsEntries(String.class, String.class).matchesSafely(map), is(false));
		assertThat(MapMatcher.noEntries(String.class, String.class).matchesSafely(map), is(false));
	}

	@Test
	public void testMatchesSafelyWithKeyMatcherFails() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry(equalTo("bar"), "bar").entry("08", "15").matchesSafely(map), is(false));
		assertThat(containsEntries(String.class, String.class).entry(equalTo("foo"), "foo").entry("08", "15").matchesSafely(map), is(false));
	}

	@Test
	public void testMatchesSafelyWithValueMatcherFails() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry("bar", equalTo("bar")).entry("08", "15").matchesSafely(map), is(false));
		assertThat(containsEntries(String.class, String.class).entry("foo", equalTo("foo")).entry("08", "15").matchesSafely(map), is(false));
	}

	@Test
	public void testMatchesSafelyDoubleFailure() throws Exception {
		Map<String, String> map = map("foo", "bar", "08", "15");

		assertThat(containsEntries(String.class, String.class).entry("bar", equalTo("bar")).entry(equalTo("09"), "15").matchesSafely(map), is(false));
		assertThat(containsEntries(String.class, String.class).entry("foo", equalTo("foo")).entry("08", equalTo("16")).matchesSafely(map), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).entry("foo", "bar").entry("08", "15").describeTo(description);
		
		assertThat(description.toString(), equalTo("<{\"foo\"=\"bar\", \"08\"=\"15\"}>"));
	}

	@Test
	public void testDescribeMismatchEntry() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).entry("foo", "bar").entry("08", "15").describeMismatch(map("foo","bar","47","11"), description);
		
		assertThat(description.toString(), equalTo("missing entries <{\"08\"=\"15\"}>, unmatched entries <{was \"47\"=was \"11\"}>"));
	}

	@Test
	public void testDescribeMismatchUnmatched() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).entry("foo", "bar").entry("08", "15").describeMismatch(map("foo","bar","08","15","47","11"), description);
		
		assertThat(description.toString(), equalTo("unmatched entries <{was \"47\"=was \"11\"}>"));
	}

	@Test
	public void testDescribeMismatchMissing() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).entry("foo", "bar").entry("08", "15").describeMismatch(map("foo","bar"), description);
		
		assertThat(description.toString(), equalTo("missing entries <{\"08\"=\"15\"}>"));
	}

	@Test
	public void testDescribeMismatchMissingOnEmpty() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).describeMismatch(map("47","11"), description);
		
		assertThat(description.toString(), equalTo("unmatched entries <{was \"47\"=was \"11\"}>"));
	}

	@Test
	public void testDescribeMismatchUnmatchedWithCustomMatcher() throws Exception {
		StringDescription description = new StringDescription();
		
		containsEntries(String.class, String.class).entry(custom("foo"), custom("bar")).entry(custom("08"), custom("15")).describeMismatch(map("foo","bar","08","15","47","11"), description);
		
		assertThat(description.toString(), equalTo("unmatched entries <{custom \"47\"=custom \"11\"}>"));
	}
	

	private Matcher<String> custom(String string) {
		return new IsEqual<String>(string) {
			@Override
			public void describeMismatch(Object item, Description description) {
				description.appendText("custom ").appendValue(item);
			}
		};
	}

	private Map<String, String> map(String... elements) {
		Map<String, String> map = new HashMap<String, String>();
		String key = null;
		boolean nullValue = false;
		for (String element : elements) {
			if (key == null && !nullValue) {
				if (element == null) {
					nullValue = true;
				} else {
					key = element;
				}
			} else {
				map.put(key, element);
				key = null;
				nullValue = false;
			}
		}
		return map;
	}

}
