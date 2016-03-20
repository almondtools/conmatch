package com.almondtools.conmatch.exceptions;

public final class Exceptions {
	
	private Exceptions() {
	}

	public static Throwable catchException(WithResult<?> code) {
		return catchException(code, Throwable.class);
	}
	
	public static <T extends Throwable> T catchException(WithResult<?> code, Class<T> clazz) {
		try {
			code.run();
			return null;
		} catch (Throwable exception) {
			if (clazz.isInstance(exception)) {
				return clazz.cast(exception);
			} else {
				return null;
			}
		}
	}
	
	public static Throwable catchException(WithoutResult code) {
		return catchException(code, Throwable.class);
	}
	
	public static <T extends Throwable> T catchException(WithoutResult code, Class<T> clazz) {
		try {
			code.run();
			return null;
		} catch (Throwable exception) {
			if (clazz.isInstance(exception)) {
				return clazz.cast(exception);
			} else {
				return null;
			}
		}
	}
	
	
	public interface WithResult<T> {
		T run() throws Throwable;
	}
	
	public interface WithoutResult {
		void run() throws Throwable;
	}
}
