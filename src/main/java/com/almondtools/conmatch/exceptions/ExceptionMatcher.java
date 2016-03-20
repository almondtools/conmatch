package com.almondtools.conmatch.exceptions;

import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

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
		this.message = equalTo(message);
		return this;
	}

	public ExceptionMatcher<T> withMessage(Matcher<? super String> message) {
		this.message = message;
		return this;
	}

	public ExceptionMatcher<T> withCause(Throwable cause) {
		this.cause = equalTo(cause);
		return this;
	}

	public ExceptionMatcher<T> withCause(Matcher<? extends Throwable> cause) {
		this.cause = cause;
		return this;
	}

	@Override
	public void describeTo(Description description) {
		String prefix = "with ";
		if (clazz != null) {
			description.appendText(prefix).appendText("class ").appendValue(clazz.getSimpleName());
			prefix = " and ";
		}
		if (message != null) {
			description.appendText(prefix).appendText("message ").appendValue(message);
			prefix = " and ";
		}
		if (cause != null) {
			description.appendText(prefix).appendText("cause ").appendValue(cause);
			prefix = " and ";
		}
	}

	@Override
	protected boolean matchesSafely(Throwable item, Description mismatchDescription) {
		if (item == null) {
			mismatchDescription.appendText("is null");
			return false;
		}
		if (!clazz.isInstance(item)) {
			mismatchDescription.appendText("found class ").appendValue(item.getClass().getSimpleName());
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
