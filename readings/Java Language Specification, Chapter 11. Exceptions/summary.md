# Chapter 11. Exceptions
When a program violates the semantic constraints of the Java programming language, the Java Virtual machine signals this error to the program as an _exception_.

An example of such a violation is attempting to index outside the bounds of an array, accessing memory outside of the array.

In the case of such a violation Java will cause a non-local transfer of control from the point where the exception occurred to a point that can be specified by the programmer. Non-local transfer of control in this case refers to the fact that control is not returned from called function to their callee.

An exception is said to be [_thrown_](throw_statements.md) from the point where it occurred and is said to be _caught_ at the point to which control is transferred.

Programs can also throw exceptions explicitly, using [throw](throw_statements.md) statements.

Explicit use of throw statements provides an alternative to the old-fashioned style of handling error conditions by returning funny values, such as the integer value -1 where a negative value would not normally be expected. Experience shows that too often such funny values are ignored or not checked for by callers, leading to programs that are not robust, exhibit undesirable behavior, or both.

Every exception is represented by an instance of the class Throwable or one of it's subclasses. Such an object can be used to carry information from the point at which an exception occurs to the handler that catches it. Handlers are established by catch clauses of try statements.

During the process of throwing an exception, the Java Virtual Machine abruptly completes, one by one, any expressions, statements, method and constructor invocations, initializers, and field initialization expressions that have begun but not completed execution in the current thread. This process continues until a handler is found that indicates that it handles that particular exception by naming the class of the exception. If no such handler is found, then the exception may be handled by one of a hierarchy of uncaught exception handlers - thus every effort is made to avoid letting an exception go unhandled.

The exception mechanism of the Java SE Platform is integrated with its synchronization model, so that monitors are unlocked as synchronized statements and invocations of synchronized methods complete abruptly. 

---
### Notes: Chapter 11. Exceptions

Exceptions causing a 'non-local transfer of control' to a point specified by the programmer fits our use case great.

__Definition:__ An exception is said to be _thrown_ from the point where it occurred, including where it was thrown explicitly and is said to be _caught_ at the point to which control is transferred.

__Definition:__ Every exception is represented by an instance of the class __Throwable__ or one of it's subclasses. Such an object can be used to carry information from the point at which an exception occurs to the handler that catches it. Handlers are established by __catch__ clauses of __try__ statements

__SWRRs__ should therefore be instances of __Throwable__ or one of it's [subclasses](#11.1.-The-Kinds-and-Causes-of-Exceptions).

 1. '_During the process of throwing an exception, the JVM abruptly completes, one by one, any expressions, statements, method and constructor invocations, initializers, and field initialization expressions that have begun but not completed execution in the current thread._'
 > This could imply that some code might execute even if an exception is thrown?
 2. '_This process continues until a handler is found that indicates that it handles that particular exception by naming the class of the exception or a superclass of the class of the exception._'
 > Can i assume that just because the process of abruptly completing expressions, statements, etc... is discontinued upon finding a handler it doesn't mean that non-completed sections won't continue later?
 3. '_If no such handler is found, then the exception may be handled by one of a hierarchy of uncaught exception handlers_'
 > Could uncaught exception handlers be useful?

---

# 11.1. The Kinds and Causes of Exceptions

## 11.1.1. The Kinds of Exceptions
An exception is represented by an instance of the class __Throwable__ (a direct subclass of __Object__) or one of its subclasses.
__Throwable__ and all its subclasses are, collectively, the exception cases.
The classes __Exception__ and __Error__ are direct subclasses of __Throwable__
 - __Exception__ is the superclass of all the exceptions from which ordinary programs may wish to recover.
 The class __RuntimeException__ is a direct subclass of __Exception.__ __RuntimeException__ is the superclass of all the exceptions which may be thrown for many reasons during expression evaluation, but from which recovery may still be possible.
 __RuntimeException__ and all its subclasses are, collectively, the _run-time exception classes_.
<br/>
 - __Error__ is the superclass of all the exceptions from which ordinary programs are not ordinarily expected to recover.
 __Error__ and all its subclasses are, collectively, the _error_ classes.

 The _unchecked exception classes_ are the run-time exception classes and the error classes.

 The _checked exception classes_ are all exception classes other than the unchecked exception classes. That is, the checked exception classes are __Throwable__ and all its subclasses other than __Runtime Exception__ and its subclasses and __Error__ classes.

_Programs can use the pre-existing exception classes of the Java Se Platform API in throw statements, or define additional exception classes as subclasses of Throwable or of any of its subclasses, as appropriate. To take advantage of compile-time checking for exception handlers, it it typical to define most new exception classes as checked exception classes, that is, as subclasses of Exception that are not of RuntimeException._

_The class __Error__ is a separate subclass of __Throwable__, distinct from __Exception__ in the class hierarchy, to allow programs to use the idiom "} catch (Exception e) {" to catch all exceptions from which recovery may be possible without catching errors from which recovery is typically not possible._

_Note that a subclass of __Throwable__ cannot be generic, a class is __generic__ if if the class declaration declares one or more type variables_

---

### Notes: 11.1.1 The Kinds of exceptions

__Throwable__ has 2 direct subclasses:
- __Exception__ which is the superclass of all exceptions from which ordinary programs wish to recover. This would then include __SWRRs__.
- __Errors__  being the superclass of all errors from which a program is not expected to recover. Making it unsuitable for __SWRRs__

__Exception__ is the _superclass_ of __RuntimeException__. Which is the superclass of all the exceptions which may be thrown during expression evaluation, but from which recovery may still be possible.

> __RuntimeExceptions__ sound promising for __SWRRs__ as _in-place deployment requires runtime evaluation._

> __SWRR extends RuntimeExceptions__

---

## 11.1.2. The Causes of Exceptions
An exceptions is thrown for one of four reasons:
 - A _throw_ statement was executed.
 - An enabled assert statement was executed, and evaluation of its boolean expression evaluated to _false_
 - An abnormal execution condition was synchronously detected by the Java Virtual Machine, namely:
   - Evaluation of an expression violates the normal semantics of the Java programming language, such as an integer divide by zero.
   - An error occurs while loading, linking, or initializing part of the program, in this case, an instance of a subclass of __LinkageError__ is thrown.
   - An internal error or resource limitation prevents the Java Virtual machine from implementing the semantics of the Java programming language; in this case, an instance of __VirtualMachineError__ is thrown.
   <br/>These exceptions are not thrown at an arbitrary point in the program, but rather at a point where they are specified as a possible result of an expression evaluation or statement execution.
 - An asynchronous exception occurred.

--- 
### Notes: 11.1.2. The Causes of Exceptions
It is important to know how to cause an exception for SWRRs to be able to 'cause a non-local transfer of control'.

It seems to me that the most appropriate way to cause this is through _throw_ statements such as
```Java
/*IN-PLACE DEPLOYMENT*/
Method () {
    /* Instrument at top of function */
    if(SWRREnabled(SWRROption)) 
        throw SWRRException;
}
```
```Java
/*PATCH-BASED DEPLOYMENT*/
Method () {
    /* Instrument at top of function */
    throw SWRRException;
}
```

---

## 11.1.3. Asynchronous Exceptions
Most exceptions occur synchronously as a result of an action by the thread in which they occur, and at a point in the program that is specified to possibly result in such an exception. An _asynchronous exception_ is, by contrast, an exception that can potentially occur at any point in the execution of a program.

Asynchronous exceptions occur only as a result of an internal error or resource limitation in the JVM that prevents it from implementing the semantics of the Java programming language. The asynchronous exception that is thrown is an instance of a subclass of __VirtualMachineError__.

_Note that __StackOverflowError__, a subclass of __VirtualMachineError__, may be thrown synchronously by method invocation as well as asynchronously due to native method execution or JVM resource limitations. Similarly, __OutOfMemoryError__, another subclass of __VirtualMachineError__, may be thrown synchronously during class instance creation, array creation, class initialization, and boxing conversion, as well as asynchronously._

The Java SE Platform permits a small but bounded amount of execution to occur before an asynchronous exception is thrown.

<small>_Asynchronous exceptions are rare, but proper understanding of their semantics is necessary if high-quality machine code is to be generated._</small>

---
### Note: 11.1.3. Asynchronous Exceptions
Asynchronous exceptions seem less useable for SWRRs since we need to guarantee no code is executed.

However, the fact that __StackOverflowError__ and __VirtualMachineError__ can be thrown asynchronously due to JVM resource limitations could be something that becomes important to keep in mind to ensure that edge cases are avoided.
```
Method throws StackOverflowError | VirtualMachineError {
    throw PossiblyAsyncError; <-- Could cause an edge case ?
}
``` 
Similarly the __OutOfmemoryError__ _'may be thrown asynchronously'_ and should be used with caution.

---

## Compile-Time Checking of Exceptions
The java programming language requires that a program contains handlers for _checked exceptions_ which can result from execution of a method or constructor. This compile-time checking for the presence of exception handlers is designed to reduce the number of exceptions which are not properly handled. For each checked exception which is a possible result, the _throws_ clause for the method or constructor must mention the class of that exception or one of the superclasses of the class of that exception.

The checked exception classes named in the throws clause are part of the contract between the implementor and user of the method or constructor. The _throws_ clause of an overriding method may not specify that this method will result in throwing any checked exception which the overridden method is not permitted, by its _throws_ clause, to throw. When interfaces are involved, more than one method declaration may be overridden by a single overriding declaration. In this case, the overriding declaration must have a _throws_ clause that is compatible with all the overridden declarations.

The unchecked exception classes are exempted from compile-time checking, but sophisticated programs may yet wish to catch and attempt to recover from some of these conditions.

---



