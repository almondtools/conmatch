conmatch
========

conmatch is a java hamcrest library usable for unit test. The original idea was to provide matchers that assert programming conventions, some other helpful matchers were added:

* `UtilityClassMatcher` asserts a common convention on utility classes
  * `final` class
  * `private` default constructor (this one is covered by the test)
  * `static` methods
* `EqualityMatcher` allows to assert (cover) the default conventions easily and add custom equality, default are:
  * no object should equal `null`
  * no object should equal an object of anothe class
  * every object should equal `this`
* `EnumMatcher` asserts that the argument is an `enum` and `valueOf` is at least once called 

* `PrimitiveArrayMatcher` allows to match primitive (e.g. int, double, char) arrays
* `MapMatcher` allows to match multiple entries in a map

* `WildcardStringMatcher` allows to match strings containing wildcard patterns (? = single wildcard, * = multiple wildcards)

* `ExceptionMatcher` allows to match an exception by class, name and cause 


Using conmatch
==============

Maven Dependency
----------------

```xml
<dependency>
	<groupId>com.github.almondtools</groupId>
	<artifactId>conmatch</artifactId>
	<version>0.1.7</version>
</dependency>
```