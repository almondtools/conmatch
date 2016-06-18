package com.almondtools.conmatch.strings;

import static com.almondtools.conmatch.strings.WildcardStringMatcher.containsPattern;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class WildcardStringMatcherTest {

	@Test
	public void testMatchesSafelyWithOneCharWildcard() throws Exception {
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, 'a', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, 'Z', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '1', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '%', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, 'ß', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, ' ', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '\n', is matched"), is(true));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '', is matched"), is(true));
	}

	@Test
	public void testMatchesSafelyWithOneCharWildcardFails() throws Exception {
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, 'ab', is matched"), is(false));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, 'ZZ', is matched"), is(false));
		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '\t\n', is matched"), is(false));

		assertThat(containsPattern("just 1 char, '?', is matched").matches("just 1 char, '\t'\n, is matched"), is(false));
	}

	@Test
	public void testMatchesSafelyWithMultiCharWildcard() throws Exception {
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, 'abcde', are matched"), is(true));
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, 'ZYX', are matched"), is(true));
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, 'errare humanum est', are matched"), is(true));
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, 'with\nsome\twhitespace chars', are matched"), is(true));
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, '', are matched"), is(true));
	}

	@Test
	public void testMatchesSafelyWithMultiCharWildcardFails() throws Exception {
		assertThat(containsPattern("any chars, '*', are matched").matches("any chars, 'abcde'\n, are matched"), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();
		
		containsPattern("just 1 char, '?', is matched").describeTo(description);
		
		assertThat(description.toString(), equalTo("contains \"just 1 char, '?', is matched\""));
	}

	@Test
	public void testDescribeMismatch() throws Exception {
		StringDescription description = new StringDescription();
		
		containsPattern("just 1 char, '?', is matched").describeMismatch("just 1 char, 'ab', is matched",  description);
		
		assertThat(description.toString(), equalTo("was \"just 1 char, 'ab', is matched\""));
	}

}
