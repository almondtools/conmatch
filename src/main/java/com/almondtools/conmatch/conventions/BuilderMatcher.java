package com.almondtools.conmatch.conventions;

import static java.lang.Character.toLowerCase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.almondtools.conmatch.util.NameMapper;

public class BuilderMatcher<B, O> extends TypeSafeDiagnosingMatcher<O> {

	private Class<B> builder;
	private boolean complete;
	private NameMapper resolver;

	public BuilderMatcher(Class<B> builder) {
		this(builder, new DefaultNameMapper(), false);
	}

	public BuilderMatcher(Class<B> builder, boolean complete) {
		this(builder, new DefaultNameMapper(), complete);
	}

	public BuilderMatcher(Class<B> builder, NameMapper resolver, boolean complete) {
		this.builder = builder;
		this.resolver = resolver;
		this.complete = complete;
	}

	@Override
	public void describeTo(Description description) {
	}

	@Override
	protected boolean matchesSafely(O item, Description mismatchDescription) {
		try {
			B builderInstance = builder.newInstance();
			Map<String, Object> properties = new LinkedHashMap<>();
			for (Method method : builder.getMethods()) {
				if (method.getParameterTypes().length == 1) {
					String property = resolver.map(method.getName());
					if (property != null) {
						Object value = getValueFor(item, property);
						method.invoke(builderInstance, value);
						properties.put(property, value);
					}
				}
			}
			Method build = builder.getMethod(resolver.unMap("build"));
			Object result = build.invoke(builderInstance);
			if (complete) {
				return item.equals(result);
			} else {
				for (Map.Entry<String, Object> entry : properties.entrySet()) {
					String name = entry.getKey();
					Object expected = entry.getValue();
					Object found = getValueFor(result, name);
					if (expected == null) {
						return found == null;
					} else if (!expected.equals(found)) {
						return false;
					}
				}
				return true;
			}
		} catch (ReflectiveOperationException e) {
			return false;
		}
	}

	private Object getValueFor(Object item, String property) throws ReflectiveOperationException {
		try {
			Field field = item.getClass().getDeclaredField(property);
			field.setAccessible(true);
			return field.get(item);
		} catch (NoSuchFieldException e) {
			String getterName = "get" + Character.toUpperCase(property.charAt(0)) + property.substring(1);
			Method getter = item.getClass().getDeclaredMethod(getterName);
			return getter.invoke(item);
		}
	}

	private static class DefaultNameMapper implements NameMapper {

		@Override
		public String map(String name) {
			if (name.startsWith("with")) {
				return toLowerCase(name.charAt(4)) + name.substring(5);
			} else {
				return null;
			}
		}

		@Override
		public String unMap(String name) {
			if (name.equals("build")) {
				return "build";
			} else {
				return null;
			}
		}

	}

	public static <B, O> BuilderMatcher<B, O> buildFrom(Class<B> builder) {
		return new BuilderMatcher<>(builder, true);
	}

	public static <B, O> BuilderMatcher<B, O> partiallyBuildFrom(Class<B> builder) {
		return new BuilderMatcher<>(builder);
	}
}
