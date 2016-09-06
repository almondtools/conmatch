package com.almondtools.conmatch.datatypes;

import static com.almondtools.conmatch.datatypes.ArrayMatcher.arrayContaining;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class ArrayMatcherTest {

	@Test
	public void testMatchesSafelyWithValues() throws Exception {
		String[] set = array("foo", "bar");

		assertThat(arrayContaining(String.class, "foo", "bar").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithoutValues() throws Exception {
		String[] set = array();

		assertThat(arrayContaining(String.class).matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithMatcher() throws Exception {
		String[] set = array("foo", "bar");

		assertThat(arrayContaining(String.class, "foo", equalTo("bar")).matchesSafely(set), is(true));
		assertThat(arrayContaining(String.class, equalTo("foo"), "bar").matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithNulls() throws Exception {
		String[] set = array("foo", null);

		assertThat(arrayContaining(String.class, "foo", (String) null).matchesSafely(set), is(true));
		assertThat(arrayContaining(String.class, "foo", nullValue()).matchesSafely(set), is(true));
	}

	@Test
	public void testMatchesSafelyWithValuesFails() throws Exception {
		String[] set = array("foo", "bar");

		assertThat(arrayContaining(String.class, "foo").matchesSafely(set), is(false));
		assertThat(arrayContaining(String.class, "bar").matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithWrongSequenceFails() throws Exception {
		String[] set = array("foo", "bar");

		assertThat(arrayContaining(String.class, "bar", "foo").matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithoutValuesFails() throws Exception {
		String[] set = array("foo");

		assertThat(arrayContaining(String.class).matchesSafely(set), is(false));
	}

	@Test
	public void testMatchesSafelyWithMatcherFails() throws Exception {
		String[] set = array("foo", "bar");

		assertThat(arrayContaining(String.class, equalTo("foobar"), equalTo("foo")).matchesSafely(set), is(false));
		assertThat(arrayContaining(String.class, equalTo("foo"), equalTo("foobar")).matchesSafely(set), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class, "foo", "bar").describeTo(description);

		assertThat(description.toString(), equalTo("<[\"foo\", \"bar\"]>"));
	}

	@Test
	public void testDescribeMismatchEntry() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class, "foo", "bar").describeMismatch(array("foo", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., was \"foobar\"]>"));
	}

	@Test
	public void testDescribeMismatchUnmatched() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class, "foo", "bar").describeMismatch(array("foo", "bar", "foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[.., found 1 elements surplus [was \"foobar\"]]>"));
	}

	@Test
	public void testDescribeMismatchMissing() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class, "foo", "bar").describeMismatch(array("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[., missing 1 elements]>"));
	}

	@Test
	public void testDescribeMismatchMissingOnEmpty() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class).describeMismatch(array("foo"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[found 1 elements surplus [was \"foo\"]]>"));
	}
	
	@Test
	public void testDescribeMismatchUnmatchedWithCustomMatcher() throws Exception {
		StringDescription description = new StringDescription();

		arrayContaining(String.class, custom("foo")).describeMismatch(array("foobar"), description);

		assertThat(description.toString(), equalTo("mismatching elements <[custom \"foobar\"]>"));
	}

	private Matcher<String> custom(String string) {
		return new IsEqual<String>(string) {
			@Override
			public void describeMismatch(Object item, Description description) {
				description.appendText("custom ").appendValue(item);
			}
		};
	}

	private String[] array(String... elements) {
		return elements;
	}

}
