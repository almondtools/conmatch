package com.almondtools.conmatch.conventions;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ReflectiveEqualsMatcher<T> extends TypeSafeMatcher<T> {

	private T object;

	public ReflectiveEqualsMatcher(T object) {
		this.object = object;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("should reflectively equal the given object: " + object.toString());
	}

	@Override
	protected boolean matchesSafely(T item) {
		try {
			if (object.getClass() != item.getClass()) {
				return false;
			}
			Set<Comparison> done = new HashSet<>();
			Queue<Comparison> todo = new LinkedList<>();
			todo.add(new Comparison(object.getClass(), object, item));
			while (!todo.isEmpty()) {
				Comparison current = todo.remove();
				if (done.contains(current)) {
					continue;
				} else {
					done.add(current);
				}
				Object left = current.left;
				Object right = current.right;
				Field[] fields = current.clazz.getFields();
				for (Field field : fields) {
					Object leftField = field.get(left);
					Object rightField = field.get(right);
					if (leftField == null && rightField == null) {
						continue;
					} else if (leftField == null) {
						return false;
					} else if (rightField == null) {
						return false;
					} else if (leftField.getClass() != rightField.getClass()) {
						return false;
					} else {
						todo.add(new Comparison(leftField.getClass(), leftField, rightField));
					}
				}
			}
			return true;
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	public static <T> ReflectiveEqualsMatcher<T> reflectiveEqualTo(T object) {
		return new ReflectiveEqualsMatcher<T>(object);
	}

	private static class Comparison {

		private Class<?> clazz;
		public Object left;
		public Object right;

		public Comparison(Class<?> clazz, Object left, Object right) {
			this.clazz = clazz;
			this.left = left;
			this.right = right;
		}

		@Override
		public int hashCode() {
			return left.hashCode() + right.hashCode();
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
			Comparison that = (Comparison) obj;
			return this.clazz == that.clazz
				&& this.left == that.left
				&& this.right == that.right;
		}

	}
}
