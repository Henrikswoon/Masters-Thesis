# Annotations

Annotations can be used ...
 - By the compiler to detect errors or suppress warnings.
 - For compile-time and deployment-time processing -- Software tools can process annotation information to generate code, XML files, and so forth.
 - For runtime processing -- Some annotations are available to be examined at runtime

 ### The Format of an Annotation
 ```java
 @Entity
 ```
The <code>(@)</code> sign indicates to the compiler that what follows is an annotation. In the following example, the annotation's name is _'Override'_

 ```java
 @Override
 void mySuperMethod() {...}
 ```
Annotations can include _elements, which can be named or unnamed, and there are values for those elements
```java
@Author(
    name = "Melker Henriksson"
    date = "Today"
)
class MyClass { ... }
```

or alternatively

```java
@SuppressWarnings(value = "unchecked")
class MyClass { ... }
```

Which is interchangeable with the following since annotations with a single element are defaulted as value.

```java
@SuppressWarnings("unchecked")
class MyClass { ... }
```

If the annotation has no elements, then the parantheses can be omitted, as shown in the <code>@Override</code> example. 
It is also possible to use multiple annotations on the same declaration

```java
@Author(
    name = "Melker Henriksson"
    date = "Today"
)
@Author(
    name = "Melker Henriksson"
    date = "Yesterday"
)
class MyClass { ... }
```
Or

```java
@Author(
    name = "Melker Henriksson"
    date = "Today"
)
@EBook
class MyClass { ... }
```

#### Where Annotations Can Be Used
Annotations can be applied to declarations: declarations of classes, fields, methods, and other program elements. When used on a declaration, each annotation often appears, by convention, on its own line.

As of the Java SE 8 release, annotations can also be applied to the use of types. Here are some examples: 

##### Class instance creation expression:
```java
new @Interned MyObject();
```

##### Type cast:
```java
myString = (@NonNull String) str;
```

##### Implements clause:
```java
class UnmodifiableList<T> implements
    @Readonly List<@Readonly T> { ... }
```

###### Thrown exception declaration:
```java
void monitorTemperature() throws
    @Critical TemperatureException { ... }
```

This form of annotation is called a _type annotation_

### Declaring an Annotation Type
```java
@interface ClassPreamble {
    String author();
    String date();
    int currentRevision() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    String[] reviewers();
}
```
This is an example of a typical <code>ClassPreamble</code> annotation.

Perhaps, some Talos annotation feeding information to the compiler would look as the following

```java
@interface InstrumentableTalos {
    String[] ThrowsChecked() default Null;
    String[] ThrowsUnchecked() default { ... };
    Boolean LoggingHeuristic() default false;   // Perhaps original heuristics should be discarded.
    Boolean NullHeuristic() default false;      // Could but shouldn't become positive, assuming error handling.
    //...
}
```

The <code>@Retention</code> annotation specifies how the marked annotation is stored:
If we want annotations to be available to the JVM during runtime then we should use <code>@Retention, RetentionPolicy.RUNTIME</code>

The <code>@Target</code> annotation marks another annotation to restrict what kind of Java elements the annotation can be applied to. A target annotation specifies one of the following element types as it's value. 

The ones i have identified as particularly important are 
 - <code>ElementType.ANNOTATION_TYPE</code> 
 - <code>ElementType.CONSTRUCTOR</code>     
 - <code>ElementType.METHOD</code>
 - <code>ElementType.MODULE</code>
 - <code>ElementType.PACKAGE</code>

The <code>@Inherited</code> indicates that the annotation type can be inherited from the super class. 