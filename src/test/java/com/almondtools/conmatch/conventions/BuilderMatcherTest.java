package com.almondtools.conmatch.conventions;

import static com.almondtools.conmatch.conventions.BuilderMatcher.buildFrom;
import static com.almondtools.conmatch.conventions.BuilderMatcher.partiallyBuildFrom;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Objects;

import org.junit.Test;

public class BuilderMatcherTest {

	@Test
	public void testMatchesSafelyPartiallyBuildFrom() throws Exception {
		assertThat(partiallyBuildFrom(MyBuilder.class).matches(new MyObject("foo", 1)), is(true));
	}

	@Test
	public void testMatchesSafelyPartiallyBuildFromOnIncomplete() throws Exception {
		assertThat(partiallyBuildFrom(MyIncompleteBuilder.class).matches(new MyObject("foo", 1)), is(true));
	}

	@Test
	public void testMatchesSafelyPartiallyBuildFromFailsOnNonBuilder() throws Exception {
		assertThat(partiallyBuildFrom(MyNonBuilder.class).matches(new MyObject("foo", 1)), is(false));
	}

	@Test
	public void testMatchesSafelyPartiallyBuildFromFailsOnNonBuilderWithNull() throws Exception {
		assertThat(partiallyBuildFrom(MyNonBuilder.class).matches(new MyObject(null, 1)), is(false));
	}

	@Test
	public void testMatchesSafelyBuildFrom() throws Exception {
		assertThat(buildFrom(MyBuilder.class).matches(new MyObject("foo", 1)), is(true));
	}

	@Test
	public void testMatchesSafelyBuildFromFailsOnIncomplete() throws Exception {
		assertThat(buildFrom(MyIncompleteBuilder.class).matches(new MyObject("foo", 1)), is(false));
	}

	@Test
	public void testMatchesSafelyPartiallyBuildFromWithNull() throws Exception {
		assertThat(partiallyBuildFrom(MyIncompleteBuilder.class).matches(new MyObject(null, 1)), is(true));
	}

	private static class MyObject {

		private String string;
		private int i;

		public MyObject() {
		}

		public MyObject(String string, int i) {
			this.string = string;
			this.i = i;
		}

		@Override
		public int hashCode() {
			return string.hashCode() + i;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			MyObject that = (MyObject) obj;
			return Objects.equals(this.i, that.i)
				&& Objects.equals(this.string, that.string);
		}
		
		
	}

	@SuppressWarnings("unused")
	private static class MyBuilder {

		private MyObject object;

		public MyBuilder() {
			this.object = new MyObject();
		}

		public MyBuilder withString(String string) {
			object.string = string;
			return this;
		}

		public MyBuilder withI(int i) {
			object.i = i;
			return this;
		}

		public MyObject build() {
			return object;
		}
	}

	@SuppressWarnings("unused")
	private static class MyIncompleteBuilder {

		private MyObject object;

		public MyIncompleteBuilder() {
			this.object = new MyObject();
		}

		public MyIncompleteBuilder withString(String string) {
			object.string = string;
			return this;
		}

		public MyObject build() {
			return object;
		}
	}

	@SuppressWarnings("unused")
	private static class MyNonBuilder {

		private MyObject object;

		public MyNonBuilder() {
			this.object = new MyObject();
		}

		public MyNonBuilder withString(String string) {
			object.string = string + string;
			return this;
		}

		public MyObject build() {
			return object;
		}
	}
}
