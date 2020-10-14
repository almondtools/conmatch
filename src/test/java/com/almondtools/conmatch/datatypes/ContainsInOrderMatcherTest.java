package com.almondtools.conmatch.datatypes;

import static com.almondtools.conmatch.datatypes.ContainsInOrderMatcher.containsInOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class ContainsInOrderMatcherTest {

	@Test
	public void testMatchesSafelyWithValues() throws Exception {
		List<String> set = list("foo", "bar");

		assertThat(containsInOrder(String.class, "foo", "bar").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithoutValues() throws Exception {
		List<String> set = list();

		assertThat(containsInOrder(String.class).matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithMatcher() throws Exception {
		List<String> set = list("foo", "bar");

		assertThat(containsInOrder(String.class, "foo", equalTo("bar")).matchesSafely(set), is(true));
		assertThat(containsInOrder(String.class, equalTo("foo"), "bar").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithNulls() throws Exception {
		List<String> set = list("foo", null);

		assertThat(containsInOrder(String.class, "foo", (String) null).matchesSafely(set), is(true));
		assertThat(containsInOrder(String.class, "foo", nullValue()).matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithValuesFails() throws Exception {
		List<String> set = list("foo", "bar");

		assertThat(containsInOrder(String.class, "foo").matchesSafely(set), is(false));
		assertThat(containsInOrder(String.class, "bar").matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithWrongSequenceFails() throws Exception {
		List<String> set = list("foo", "bar");

		assertThat(containsInOrder(String.class, "bar", "foo").matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithoutValuesFails() throws Exception {
		List<String> set = list("foo");

		assertThat(containsInOrder(String.class).matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithMatcherFails() throws Exception {
		List<String> set = list("foo", "bar");

		assertThat(containsInOrder(String.class, equalTo("foobar"), equalTo("foo")).matchesSafely(set), is(false));
		assertThat(containsInOrder(String.class, equalTo("foo"), equalTo("foobar")).matchesSafely(set), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class, "foo", "bar").describeTo(description);

		assertThat(description.toString(), equalTo("<[\"foo\", \"bar\"]>"));
	}

	@Test
	public void testDescribeMismatchEntry() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class, "foo", "bar").describeMismatch(list("foo", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., was \"foobar\"]>"));
	}

	@Test
	public void testDescribeMismatchUnmatched() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class, "foo", "bar").describeMismatch(list("foo", "bar", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[.., found 1 elements surplus [was \"foobar\"]]>"));
	}

	@Test
	public void testDescribeMismatchMissing() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class, "foo", "bar").describeMismatch(list("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., missing 1 elements]>"));
	}

	@Test
	public void testDescribeMismatchMissingOnEmpty() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class).describeMismatch(list("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[found 1 elements surplus [was \"foo\"]]>"));
	}
	
	@Test
	public void testDescribeMismatchUnmatchedWithCustomMatcher() throws Exception {
		StringDescription description = new StringDescription();

		containsInOrder(String.class, custom("foo")).describeMismatch(list("foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[custom \"foobar\"]>"));
	}

	@Test
	public void testTypes() throws Exception {
		assertThat(new HashSet<Sub>(), containsInOrder(Sub.class));
		assertThat(new ArrayList<Sub>(), containsInOrder(Sub.class));
		assertThat(new HashSet<Sub>(), containsInOrder(Super.class));
		assertThat(new ArrayList<Sub>(), containsInOrder(Super.class));
	}

	private Matcher<String> custom(String string) {
		return new IsEqual<String>(string) {
			@Override
			public void describeMismatch(Object item, Description description) {
				description.appendText("custom ").appendValue(item);
			}
		};
	}

	private List<String> list(String... elements) {
		List<String> list = new ArrayList<String>();
		for (String element : elements) {
			list.add(element);
		}
		return list;
	}

	private static class Super {
	}

	private static class Sub extends Super {
	}

}
