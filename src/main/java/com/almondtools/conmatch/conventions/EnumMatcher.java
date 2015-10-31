package com.almondtools.conmatch.conventions;

import java.lang.reflect.Method;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class EnumMatcher extends TypeSafeDiagnosingMatcher<Class<? extends Enum<?>>> {

	private Integer count;

	public EnumMatcher() {
	}

	public EnumMatcher withElements(int count) {
		this.count = count;
		return this;
	}

	public static EnumMatcher isEnum() {
		return new EnumMatcher();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("should be an enum");
		description.appendText("\nand additional constraints:");
		if (count != null) {
			description.appendText("\n- number of elements should be ").appendValue(count);
		}
	}

	@Override
	protected boolean matchesSafely(Class<? extends Enum<?>> item, Description mismatchDescription) {
		if (!Enum.class.isAssignableFrom(item)) {
			mismatchDescription.appendText("is not an enum");
			return false;
		}
		Enum<?>[] enumConstants=item.getEnumConstants();
		if (count != null && enumConstants.length != count) {
			mismatchDescription.appendText("number of elements was ").appendValue(item.getEnumConstants().length);
			return false;
		}
		for (Enum<?> enumConstant : enumConstants) {
			try {
				Method valueOf = item.getDeclaredMethod("valueOf", String.class);
				Object result = valueOf.invoke(null, enumConstant.name());
				if (result != enumConstant) {
					mismatchDescription.appendText("valueOf(\"" + enumConstant.name() + "\n != " + enumConstant.getClass().getSimpleName() + "."  + enumConstant.name());
					return false;
				}
			} catch (ReflectiveOperationException | SecurityException e) {
				mismatchDescription.appendText("fails with " + e.getMessage());
				return false;
			}
		}
		return true;
	}

}
