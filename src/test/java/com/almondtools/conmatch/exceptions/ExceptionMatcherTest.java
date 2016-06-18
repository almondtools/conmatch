package com.almondtools.conmatch.exceptions;

import static com.almondtools.conmatch.exceptions.ExceptionMatcher.matchesException;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class ExceptionMatcherTest {

	@Test
	public void testMatchesSafelyOnType() throws Exception {
		assertThat(matchesException(RuntimeException.class).matches(new RuntimeException()), is(true));
	}
	
	@Test
	public void testMatchesSafelyOnSubtype() throws Exception {
		assertThat(matchesException(RuntimeException.class).matches(new IllegalArgumentException()), is(true));
	}

	@Test
	public void testMatchesSafelyOnTypeAndMessage() throws Exception {
		assertThat(matchesException(RuntimeException.class).withMessage("my message").matches(new RuntimeException("my message")), is(true));
	}
	
	@Test
	public void testMatchesSafelyOnTypeAndCause() throws Exception {
		assertThat(matchesException(RuntimeException.class).withCause(RuntimeException.class).matches(new RuntimeException(new RuntimeException("my message"))), is(true));
	}
	
	@Test
	public void testMatchesSafelyFailsOnNull() throws Exception {
		assertThat(matchesException(RuntimeException.class).matches(null), is(false));
	}

	@Test
	public void testMatchesSafelyFailsOnType() throws Exception {
		assertThat(matchesException(RuntimeException.class).matches(new Exception()), is(false));
	}
	
	@Test
	public void testMatchesSafelyFailsOnMessage() throws Exception {
		assertThat(matchesException(RuntimeException.class).withMessage("expected message").matches(new RuntimeException("other message")), is(false));
	}
	
	@Test
	public void testMatchesSafelyFailsOnCause() throws Exception {
		assertThat(matchesException(RuntimeException.class).withCause(IllegalArgumentException.class).matches(new IllegalStateException()), is(false));
	}

	@Test
	public void testDescribeToWithType() throws Exception {
		StringDescription description = new StringDescription();
		
		matchesException(RuntimeException.class).describeTo(description);
		
		assertThat(description.toString(), equalTo("of type <RuntimeException>"));
	}

	@Test
	public void testDescribeToWithTypeAndMessage() throws Exception {
		StringDescription description = new StringDescription();
		
		matchesException(RuntimeException.class).withMessage("expected message").describeTo(description);
		
		assertThat(description.toString(), equalTo("of type <RuntimeException> with message <\"expected message\">"));
	}

	@Test
	public void testDescribeToWithTypeAndCause() throws Exception {
		StringDescription description = new StringDescription();
		
		matchesException(RuntimeException.class).withCause(RuntimeException.class).describeTo(description);
		
		assertThat(description.toString(), equalTo("of type <RuntimeException> with cause <an instance of java.lang.RuntimeException>"));
	}

}
