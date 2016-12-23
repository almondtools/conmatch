package com.almondtools.conmatch.datatypes;

import java.util.Arrays;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.almondtools.conmatch.util.SimpleClass;

public class PrimitiveArrayMatcher<T> extends TypeSafeMatcher<T> {

	private T array;
	private boolean anyOrder;

	private PrimitiveArrayMatcher(T array) {
		this.array = array;
	}

	public static PrimitiveArrayMatcher<boolean[]> booleanArrayContaining(boolean... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static PrimitiveArrayMatcher<char[]> charArrayContaining(char... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static PrimitiveArrayMatcher<byte[]> byteArrayContaining(byte... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static PrimitiveArrayMatcher<short[]> shortArrayContaining(short... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static PrimitiveArrayMatcher<int[]> intArrayContaining(int... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static Matcher<float[]> floatArrayContaining(float... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static PrimitiveArrayMatcher<long[]> longArrayContaining(long... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public static Matcher<double[]> doubleArrayContaining(double... items) {
		return new PrimitiveArrayMatcher<>(items);
	}

	public PrimitiveArrayMatcher<T> inAnyOrder() {
		Class<?> type = array.getClass().getComponentType();
		if (type == boolean.class) {
			sort((boolean[]) array);
		} else if (type == char.class) {
			Arrays.sort((char[]) array);
		} else if (type == byte.class) {
			Arrays.sort((byte[]) array);
		} else if (type == short.class) {
			Arrays.sort((short[]) array);
		} else if (type == int.class) {
			Arrays.sort((int[]) array);
		} else if (type == float.class) {
			Arrays.sort((float[]) array);
		} else if (type == long.class) {
			Arrays.sort((long[]) array);
		} else if (type == double.class) {
			Arrays.sort((double[]) array);
		}

		this.anyOrder = true;
		return this;
	}

	private void sort(boolean[] array) {
		int trueValues = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				trueValues++;
			}
		}
		for (int i = 0; i < trueValues; i++) {
			array[i] = true;
		}
		for (int i = trueValues; i < array.length; i++) {
			array[i] = false;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("an array containing values of type ")
				.appendValue(array.getClass().getComponentType())
				.appendText(": ")
				.appendValue(array);
	}

	@Override
	protected void describeMismatchSafely(T item, Description mismatchDescription) {
		if (!item.getClass().isArray()) {
			mismatchDescription.appendText("not an array");
		} else if (!item.getClass().getComponentType().isPrimitive()) {
			mismatchDescription.appendText("not a primitive array");
		} else if (item.getClass() != array.getClass()) {
			mismatchDescription.appendText("of type ").appendValue(new SimpleClass(item.getClass()));
		} else {
			mismatchDescription.appendText("with items ").appendValue(item);
		}
	}

	@Override
	protected boolean matchesSafely(T item) {
		if (!item.getClass().isArray()) {
			return false;
		}
		if (!item.getClass().getComponentType().isPrimitive()) {
			return false;
		}
		if (item.getClass() != array.getClass()) {
			return false;
		}
		Class<?> type = array.getClass().getComponentType();
		if (type == boolean.class) {
			return Arrays.equals((boolean[]) array, processed((boolean[]) item));
		} else if (type == char.class) {
			return Arrays.equals((char[]) array, processed((char[]) item));
		} else if (type == byte.class) {
			return Arrays.equals((byte[]) array, processed((byte[]) item));
		} else if (type == short.class) {
			return Arrays.equals((short[]) array, processed((short[]) item));
		} else if (type == int.class) {
			return Arrays.equals((int[]) array, processed((int[]) item));
		} else if (type == float.class) {
			return Arrays.equals((float[]) array, processed((float[]) item));
		} else if (type == long.class) {
			return Arrays.equals((long[]) array, processed((long[]) item));
		} else if (type == double.class) {
			return Arrays.equals((double[]) array, processed((double[]) item));
		} else {
			return false;
		}
	}

	private boolean[] processed(boolean[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			sort(item);
		}
		return item;
	}

	private char[] processed(char[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private byte[] processed(byte[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private short[] processed(short[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private int[] processed(int[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private float[] processed(float[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private long[] processed(long[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

	private double[] processed(double[] item) {
		if (anyOrder) {
			item = Arrays.copyOf(item, item.length);
			Arrays.sort(item);
		}
		return item;
	}

}
