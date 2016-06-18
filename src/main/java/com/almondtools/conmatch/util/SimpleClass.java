package com.almondtools.conmatch.util;

public class SimpleClass {

	private Class<?> clazz;

	public SimpleClass(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}
}
