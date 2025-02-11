# Java.lang.instrument.
Provides services that allow Java programming language agents to instrument programs running on the JVM.
The mechanism for instrumentation is modification of the bytecodes of methods.

The class files that comprise an agent are packaged into a JAR file, either with the application in an executable JAR, or more commonly, as a separate JAR file called an _agent_ JAR. An attribute in the main manifest of the JAR file identifies one of the class files in the AJR file as the _agent class_. The agent class defines a special method that the JVM invokes to _start_ the agent.

Agents that are packaged with an application in an executable JAR are started at JVM startup time. Agents that are packaged into an agent JAR file may be started at JVM startup time via a command line option, or where an implementation supports it, started in a running JVM.

Agents can transform classes in arbitrary ways at load time, transform modules, or transform the bytecode of methods of already loaded classes. Developers or administrators that deploy agents, deploy applications that package an agent with the application, or use tools that load agents into a running application, are responsible for verifying the trustworthiness of each agent including the content and structure of the agent JAR file.

### Starting an agent

#### Starting an agent packaged with an application in an executable JAR file

The JAR File Specification defines manifest attributes for standalone applications that are packaged as _executable JAR files_. If an implementation supports a mechanism to start an application as an executable JAR, then the main manifest of the JAR file can include the Launcher-Agent-Class attribute to specify the binary name of the Java agent that is packaged with the application. If the attribute is present then the JVM starts the agent by loading the agent class and invoking its 'agentmain' method. The method is invoked before the application main method is invoked. The agentmain method has one of two possible signatures. The JVM first attempts to invoke the following method on the agent class:

 ```java
 public static void agentmain(String agentArgs, Instrumentation inst)
 ```
If the agent does not define this method, then the JVM will attempt to invoke:
 ```java
 public static void agentmain(String agentArgs)
 ```

The value of the agentArgs parameter is always the empty string. In the first method, the inst parameter is an Instrumentation object that the agent can use to instrument code.

The agentmain method should do any necessary initialization required to start the agent and return. If the agent cannot be started, for example the agent class cannot be loaded, the agent class does not define a conformant agentmain method, or the agentmain method throws an uncaught exception or error, the JVM will abort before the application main method is invoked.

#### Starting an agent from the command-line interface

Where an implementation provides a means to start agents from the command-line interface, an agent JAR is specified via the following command line option

 ```
    -javaagent:<jarpath>[=<options>]
 ```
 Where `<jarpath>` is the path to the agent JAR file and `<options>` is the agent options.

The main manifest of the agent JAR file must contain the attribute Premain-Class. The value of this attribute is the binary name of the agent class in the JAR file. The JVM starts the agent by loading the agent class and invoking its premain method. The method is invoked before the application main method is invoked. The premain method has one of two possible signatures. The JVM first attempts to invoke the following method on the agent class:

 ```java
 public static void premain(String agentArgs, Instrumentation inst)
 ```
If the agent class does not define this method then the JVM will attempt to invoke:
 ```java
 public static void premain(String agentArgs)
 ```
The agent is passed its agent options via the agentArgs parameter. The agent options are passed as a single string, any additional parsing should be performed by the agent itself. In the first method, the inst parameter is an Instrumentation object that the agent can use to instrument code.

If the agent cannot be started, for example the agent class cannot be loaded, the agent class does not define a conformant premain method, or the premain method throws an uncaught exception or error, the JVM will abort before the application main method is invoked.

An implementation is not required to provide a way to start agents from the command-line interface. When it does, then it supports the javaagent option as specified above. The -javaagent option as specified above. The -javaagent option may be used multiple times on the same command-line, thus starting multiple agents. The premain methods will be called in the order that the agents are specified on the command line. More than one agent may use the same `<jarpath>`.

The agent class may also have an agentmain method for use when the agent is started after in a running JVM- When the agent is started using a command-line option, the agentmain method is not invoked.

Starting an agent in a running JVM An implementation may provide a mechanism to start agents in a running JVM. The details as tp how this is initiated are implementation specific but typically the application has already started, and its main method has already been invoked. Where an implementation supports starting an agent in a running JVM, the following applies.

 1. The agent class must packaged into an agent JAR file. The main manifest of the agent JAR file must contain the attribute Agent-Class. The value of this attribute is the binary name of the agent class in the JAR file.

 2. The agent class must define a public static agentmain methods.

 3. The JVM prints a warning on the standard error stream for each agent that it attempts to start in a running JVM. If an agent was previously started (at JVM startup, or started in a running JVM), then it is implementation specific as to whether a warning is printed when attempting to start the same agent a second or subsequent time. Warnings can be disabled by means of an implementation-specific command line option.

#### Loading agent classes and the modules/classes available to the agent class
Classes loaded from the agent JAR file are loaded by the systems class loader and are members of the system class laoder's unnamed module. The system class loader typically defines the class containing the application main method too.

The classes visible to the agent class are the classes visible to the system class loader and minimally include: 
 - The classes in packages exported by the modules in the boot layer. Whether the boot layer contains all platform modules or not will depend on the initial module or how the application was started.
 - The classes that can be defined by the system class loader (typically the class path) to be members of its unnamed module.
 - Any classes that the agent arranges to be defined by the bootstrap class loader to be members of its unnamed module.

If agent classes need to link to classes in platform or other modules that are not in the boot layer then the application may need to be started in a way that ensures that these modules are in the boot layer. In the JDK implementation for example, the --add-modules command line option can be used to add modules to the set of root modules to resolve at startup.

Supporting classes that the agent arranges to be loaded by the bootstrap class loader (by means of `appendToBootstrapClassLoaderSearch` or the Boot-Class-path attribute specified below), must link only to classes defined to the bootstrap class loader. There is no guarantee that all platform classes can be defined by the boot class loader.

If a custom system class loader is configured by means of the system property java.system.class.loader as specified in the `getSystemClassLoader` method then it must define the `appendToClassPathForInstrumentation` method as specified in `appendToSystemClassLoaderSearch`. In other words, a custom system class loader must support the mechanism to add an agent JAR file to the sustem class loader search.

##### System class loader

##### Unnamed module


---
# Further reading
 - JAR File Specification
 - Interface Instrumentation
