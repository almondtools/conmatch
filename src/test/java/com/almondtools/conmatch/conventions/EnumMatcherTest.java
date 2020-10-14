package com.almondtools.conmatch.conventions;

import static com.almondtools.conmatch.conventions.EnumMatcher.isEnum;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class EnumMatcherTest {

	@Test
	public void testMatchesSafely() throws Exception {
		assertThat(isEnum().matches(MyEnum.class), is(true));
	}

	@Test
	public void testMatchesSafelyWithElements() throws Exception {
		assertThat(isEnum().withElements(2).matches(MyEnum.class), is(true));
	}

	@Test
	public void testMatchesSafelyFails() throws Exception {
		assertThat(isEnum().matches(Object.class), is(false));
	}

	@Test
	public void testMatchesSafelyFailsWithElements() throws Exception {
		assertThat(isEnum().withElements(1).matches(MyEnum.class), is(false));
	}

	@Test
	public void testDescribeTo() throws Exception {
		StringDescription description = new StringDescription();
		
		isEnum().describeTo(description);
		
		assertThat(description.toString(), equalTo("should be an enum"));
	}
	
	@Test
	public void testDescribeToWithCount() throws Exception {
		StringDescription description = new StringDescription();
		
		isEnum().withElements(2).describeTo(description);
		
		assertThat(description.toString(), equalTo("should be an enum with number of elements <2>"));
	}

	private static enum MyEnum {
		V1, V2;
	}

}
