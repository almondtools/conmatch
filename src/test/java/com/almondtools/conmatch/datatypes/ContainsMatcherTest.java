package com.almondtools.conmatch.datatypes;

import static com.almondtools.conmatch.datatypes.ContainsMatcher.contains;
import static com.almondtools.conmatch.datatypes.ContainsMatcher.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class ContainsMatcherTest {

	@Test
	public void testMatchesSafelyWithValues() throws Exception {
		Set<String> set = set("foo", "bar");

		assertThat(contains(String.class, "foo", "bar").matchesSafely(set), is(true));
		assertThat(contains(String.class, "bar", "foo").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithoutValues() throws Exception {
		Set<String> set = set();

		assertThat(contains(String.class).matchesSafely(set), is(true));
		assertThat(empty(String.class).matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithMatcher() throws Exception {
		Set<String> set = set("foo", "bar");

		assertThat(contains(String.class, "foo", equalTo("bar")).matchesSafely(set), is(true));
		assertThat(contains(String.class, equalTo("foo"), "bar").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithNulls() throws Exception {
		Set<String> set = set("foo", null);

		assertThat(contains(String.class, "foo", (String) null).matchesSafely(set), is(true));
		assertThat(contains(String.class, (String) null, "foo").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithValuesFails() throws Exception {
		Set<String> set = set("foo", "bar");

		assertThat(contains(String.class, "foo").matchesSafely(set), is(false));
		assertThat(contains(String.class, "bar").matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithoutValuesFails() throws Exception {
		Set<String> set = set("foo");

		assertThat(contains(String.class).matchesSafely(set), is(false));
		assertThat(empty(String.class).matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithMatcherFails() throws Exception {
		Set<String> set = set("foo", "bar");

		assertThat(contains(String.class, "foobar", equalTo("bar")).matchesSafely(set), is(false));
		assertThat(contains(String.class, equalTo("foobar"), equalTo("foo")).matchesSafely(set), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class, "foo", "bar").describeTo(description);

		assertThat(description.toString(), equalTo("<[\"foo\", \"bar\"]>"));
	}

	@Test
	public void testDescribeMismatchEntry() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class, "foo", "bar").describeMismatch(set("foo", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., found 1 elements surplus [was \"foobar\"], missing 1 elements]>"));
	}

	@Test
	public void testDescribeMismatchUnmatched() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class, "foo", "bar").describeMismatch(set("foo", "bar", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[.., found 1 elements surplus [was \"foobar\"]]>"));
	}

	@Test
	public void testDescribeMismatchMissing() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class, "foo", "bar").describeMismatch(set("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., missing 1 elements]>"));
	}

	@Test
	public void testDescribeMismatchMissingOnEmpty() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class).describeMismatch(set("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[found 1 elements surplus [was \"foo\"]]>"));
	}

	@Test
	public void testDescribeMismatchUnmatchedWithCustomMatcher() throws Exception {
		StringDescription description = new StringDescription();

		contains(String.class, custom("foo")).describeMismatch(set("foo", "bar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., found 1 elements surplus [custom \"bar\"]]>"));
	}

	@Test
	public void testTypes() throws Exception {
		assertThat(new HashSet<Sub>(), empty(Sub.class));
		assertThat(new ArrayList<Sub>(), empty(Sub.class));
		assertThat(new HashSet<Sub>(), empty(Super.class));
		assertThat(new ArrayList<Sub>(), empty(Super.class));
	}

	private Matcher<String> custom(String string) {
		return new IsEqual<String>(string) {
			@Override
			public void describeMismatch(Object item, Description description) {
				description.appendText("custom ").appendValue(item);
			}
		};
	}

	private Set<String> set(String... elements) {
		Set<String> set = new HashSet<String>();
		for (String element : elements) {
			set.add(element);
		}
		return set;
	}

	private static class Super {
	}

	private static class Sub extends Super {
	}

}
