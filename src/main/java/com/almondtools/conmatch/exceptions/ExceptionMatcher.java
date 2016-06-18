package com.almondtools.conmatch.exceptions;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import com.almondtools.conmatch.util.SimpleClass;

public class ExceptionMatcher<T extends Throwable> extends TypeSafeDiagnosingMatcher<Throwable> {

	private Class<T> clazz;
	private Matcher<? super String> message;
	private Matcher<? extends Throwable> cause;

	public ExceptionMatcher(Class<T> clazz) {
		this.clazz = clazz;
	}

	public static <T extends Throwable> ExceptionMatcher<T> matchesException(Class<T> clazz) {
		return new ExceptionMatcher<T>(clazz);
	}

	public ExceptionMatcher<T> withMessage(String message) {
		return withMessage(equalTo(message));
	}

	public ExceptionMatcher<T> withMessage(Matcher<? super String> message) {
		this.message = message;
		return this;
	}

	public ExceptionMatcher<T> withCause(Class<? extends Throwable> cause) {
		Matcher<Throwable> causeMatcher = instanceOf(cause);
		return withCause(causeMatcher);
	}

	public ExceptionMatcher<T> withCause(Matcher<? extends Throwable> cause) {
		this.cause = cause;
		return this;
	}

	@Override
	public void describeTo(Description description) {
			description.appendText("of type ").appendValue(new SimpleClass(clazz));
		if (message != null) {
			description.appendText(" with message ").appendValue(message);
		}
		if (cause != null) {
			description.appendText(" with cause ").appendValue(cause);
		}
	}

	@Override
	protected boolean matchesSafely(Throwable item, Description mismatchDescription) {
		if (!clazz.isInstance(item)) {
			mismatchDescription.appendText("found class ").appendValue(new SimpleClass(item.getClass()));
			return false;
		}
		if (message != null && !message.matches(item.getMessage())) {
			mismatchDescription.appendText("found message ").appendValue(item.getMessage());
			return false;
		}
		if (cause != null && !cause.matches(item.getCause())) {
			mismatchDescription.appendText("found cause ").appendValue(item.getCause());
			return false;
		}
		return true;
	}

}
