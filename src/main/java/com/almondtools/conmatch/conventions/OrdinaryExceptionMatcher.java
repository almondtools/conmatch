package com.almondtools.conmatch.conventions;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class OrdinaryExceptionMatcher<T extends Throwable> extends TypeSafeDiagnosingMatcher<Class<T>> {

	public OrdinaryExceptionMatcher() {
	}

	public static <T extends Throwable> OrdinaryExceptionMatcher<T> matchesOrdinaryException() {
		return new OrdinaryExceptionMatcher<T>();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is exception with standard constructors (empty, cause, message)");
	}

	@Override
	protected boolean matchesSafely(Class<T> item, Description mismatchDescription) {
		if (item == null) {
			mismatchDescription.appendText("is null");
			return false;
		}
		try {
			T exception = item.newInstance();
			if (exception.getCause() != null) {
				mismatchDescription.appendText(item.getSimpleName() + "() has cause that is not null: " + exception.getCause().getClass().getSimpleName());
				return false;
			}
			if (exception.getMessage() != null) {
				mismatchDescription.appendText(item.getSimpleName() + "() has message that is not null: " + exception.getMessage());
				return false;
			}
		} catch (ReflectiveOperationException e) {
			return false;
		}
		try {
			T exception = item.getConstructor(String.class, Throwable.class).newInstance("msg", new RuntimeException());
			if (!(exception.getCause() instanceof RuntimeException)) {
				mismatchDescription.appendText(item.getSimpleName() + "(String, Throwable) does not preserve cause: " + exception.getCause().getClass().getSimpleName());
				return false;
			}
			if (!"msg".equals(exception.getMessage())) {
				mismatchDescription.appendText(item.getSimpleName() + "(String, Throwable) does not preserve message: " + exception.getMessage());
				return false;
			}
		} catch (ReflectiveOperationException e) {
			return false;
		}
		try {
			T exception = item.getConstructor(String.class).newInstance("msg");
			if (exception.getCause() != null) {
				mismatchDescription.appendText(item.getSimpleName() + "(String) has cause that is not null: " + exception.getCause().getClass().getSimpleName());
				return false;
			}
			if (!"msg".equals(exception.getMessage())) {
				mismatchDescription.appendText(item.getSimpleName() + "(String) does not preserve message: " + exception.getMessage());
				return false;
			}
		} catch (ReflectiveOperationException e) {
			return false;
		}
		try {
			T exception = item.getConstructor(Throwable.class).newInstance(new RuntimeException());
			if (!(exception.getCause() instanceof RuntimeException)) {
				mismatchDescription.appendText(item.getSimpleName() + "(Throwable) does not preserve cause: " + exception.getCause().getClass().getSimpleName());
				return false;
			}
			if (!RuntimeException.class.getCanonicalName().equals(exception.getMessage())) {
				mismatchDescription.appendText(item.getSimpleName() + "(Throwable) has message that is not the name of the cause exception: " + exception.getMessage());
				return false;
			}
		} catch (ReflectiveOperationException e) {
			return false;
		}
		return true;
	}

}
