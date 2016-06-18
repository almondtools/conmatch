package com.almondtools.conmatch.strings;

import static java.util.regex.Pattern.DOTALL;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class WildcardStringMatcher extends TypeSafeMatcher<String> {

	private String pattern;

	public WildcardStringMatcher(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("contains ").appendValue(pattern);
	}

	@Override
	protected boolean matchesSafely(String item) {
		StringTokenizer t = new StringTokenizer(pattern, "?*", true);
		StringBuilder buffer = new StringBuilder();
		while (t.hasMoreTokens()) {
			String nextToken = t.nextToken();
			if ("?".equals(nextToken)) {
				buffer.append(".?");
			} else if ("*".equals(nextToken)) {
				buffer.append(".*?");
			} else {
				buffer.append(Pattern.quote(nextToken));
			}
		}
		Pattern p = Pattern.compile(buffer.toString(), DOTALL);
		Matcher m = p.matcher(item);
		return m.find();
	}

	public static WildcardStringMatcher containsPattern(String pattern) {
		return new WildcardStringMatcher(pattern);
	}

}
