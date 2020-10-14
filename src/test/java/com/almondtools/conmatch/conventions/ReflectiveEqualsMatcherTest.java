package com.almondtools.conmatch.conventions;

import static com.almondtools.conmatch.conventions.ReflectiveEqualsMatcher.reflectiveEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class ReflectiveEqualsMatcherTest {

	@Test
	public void testPrimitivesAndStrings() throws Exception {
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject()), is(true));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withBo(false)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withB((byte) 0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withS((short) 0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withI(0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withL(0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withF(0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withD(0)), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withC('0')), is(false));
		assertThat(reflectiveEqualTo(new TestObject()).matches(new TestObject().withStr("null")), is(false));
	}
	
	@Test
	public void testClass() throws Exception {
		assertThat(reflectiveEqualTo(new TestObjectWithClass()).matches(new TestObjectWithClass()), is(true));
		assertThat(reflectiveEqualTo(new TestObjectWithClass()).matches(new TestObjectWithClass().withClazz(TestObject.class)), is(false));
	}
	
	@Test
	public void testCustomBaseTypes() throws Exception {
		assertThat(reflectiveEqualTo(new TestObjectWithCustomBaseTypes()).matches(new TestObjectWithCustomBaseTypes()), is(false));
		assertThat(reflectiveEqualTo(new TestObjectWithCustomBaseTypes()).withBaseTypes(CompareObject.class).matches(new TestObjectWithCustomBaseTypes()), is(true));
	}
	
	@SuppressWarnings("unused")
	private static class TestObjectWithCustomBaseTypes {
		private CompareObject o = new CompareObject();
		
		public TestObjectWithCustomBaseTypes withO(CompareObject o) {
			this.o = o;
			return this;
		}
	}
	
	@SuppressWarnings("unused")
	private static class CompareObject {
		private static int COUNTER = 0;
		
		private int counter = COUNTER++;
		
		@Override
		public boolean equals(Object obj) {
			return true;
		}
	}
	
	@SuppressWarnings("unused")
	private static class TestObjectWithClass {
		private Class<?> clazz = TestObjectWithClass.class;
		
		public TestObjectWithClass withClazz(Class<?> clazz) {
			this.clazz = clazz;
			return this;
		}
	}
	
	@SuppressWarnings("unused")
	private static class TestObject {

		private boolean bo = true;
		private byte b = 1;
		private short s = 2;
		private int i = 3;
		private long l = 4;
		private float f = 5.0f;
		private double d = 6.0;
		private char c = '7';
		private String str = "eight";

		public TestObject withBo(boolean bo) {
			this.bo = bo;
			return this;
		}

		public TestObject withB(byte b) {
			this.b = b;
			return this;
		}

		public TestObject withS(short s) {
			this.s = s;
			return this;
		}

		public TestObject withI(int i) {
			this.i = i;
			return this;
		}

		public TestObject withL(long l) {
			this.l = l;
			return this;
		}

		public TestObject withF(float f) {
			this.f = f;
			return this;
		}

		public TestObject withD(double d) {
			this.d = d;
			return this;
		}

		public TestObject withC(char c) {
			this.c = c;
			return this;
		}

		public TestObject withStr(String str) {
			this.str = str;
			return this;
		}

	}
}
