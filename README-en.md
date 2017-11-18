[![Release](https://jitpack.io/v/AvatarQing/FLog.svg)](https://jitpack.io/#AvatarQing/FLog)

# FLog
A simple android logger with great flexibility and scalability which is based on combination of functions

# Download
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

Add the dependency in module's build.gradle
```
dependencies {
    // Only contains two core classes: ILogger and FLog
    implementation 'com.github.AvatarQing.FLog:flog-core:0.1.0'
    
    // Contains some useful implentations of ILogger
    implementation 'com.github.AvatarQing.FLog:flog-loggers:0.1.0'
}
```

# Usage
1. Create a `ILogger` instance.
2. Call `FLog.init(ILogger logger)` to initialize.
3. Then call `FLog`'s static methods everywhere throughout your app.

Check out the demo for more usage details.

# Details
There are two core classes: `ILogger` and `FLog`.

`ILogger` is a functional interface which has only one method:  
`public void log(int priority, String tag, Throwable t, Object message);`

`FLog` is a wrapper for `ILogger` which provides more helpers methods, such as `v()`,`d()`,`i()`,`w()`,`e()` and so on.

How can we print what we want? We can implement `ILogger` into various classes, each class only do one job(Single Responsibility Principle), then combine them to one instance,and pass the result to `FLog`.

Print message to logcat:
```
public class LogcatLogger implements ILogger {
    @Override
    public void log(int priority, String tag, Throwable t, Object message) {
        Log.println(priority, tag, String.valueOf(message));
    }
}
```

Add suffix to the message(such as trace or thread information):
```
public class SuffixMessageLogger implements ILogger {
    private ILogger logger;
    private IStringSupplier supplier;

    public SuffixMessageLogger(ILogger logger, IStringSupplier supplier) {
        this.logger = logger;
        this.supplier = supplier;
    }

    @Override
    public void log(int priority, String tag, Throwable t, Object message) {
        logger.log(priority, tag, t, message + supplier.get());
    }
}

@FunctionalInterface
public interface IStringSupplier {
    String get();
}
```

Add prefix to the tag:
```
public class PrefixTagLogger implements ILogger {
    private ILogger logger;
    private IStringSupplier supplier;

    public PrefixTagLogger(ILogger logger, IStringSupplier supplier) {
        this.logger = logger;
        this.supplier = supplier;
    }

    @Override
    public void log(int priority, String tag, Throwable t, Object message) {
        logger.log(priority, supplier.get() + tag, t, message);
    }
}
```

The code above are so verbose, we can use lambda expression of Java 8 to simplify it:
```
public final class Loggers {
    public static ILogger logcat() {
        return (priority, tag, t, message) -> Log.println(priority, tag, String.valueOf(message));
    }

    public static ILogger suffixMessage(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> logger.log(priority, tag, t, message + supplier.get());
    }

    public static ILogger prefixTag(ILogger logger, IStringSupplier supplier) {
        return (priority, tag, t, message) -> logger.log(priority, supplier.get() + tag, t, message);
    }
}
```

Then we can initialize the logger in simple way:
```
import static Loggers.*;

FLog.init(
        prefixTag(
                suffixMessage(
                        logcat(),
                        () -> " >>> Thread:" + Thread.currentThread().getName()
                ),
                () -> "MyApp-"
        )
);
```

Print a log:
```
FLog.d("test", "Hello World");
```

The logcat will print the message like:
```
11-16 09:56:34.533 9924-9924/com.avatarqing.tools.log.demo D/MyApp-test: Hello World >>> Thread:main
```

It seems a little complicated, but it can adapt to the change very well. When you want to print log in other ways, just define more unit operations and adjust the combination. This fits the "Open/Closed Principle".

For example, we suddenly want to print thread information before the message(just like “Thread:main >>> Hello World”), we can do:
```
public static ILogger prefixMessage(ILogger logger, IStringSupplier supplier) {
	return (priority, tag, t, message) -> logger.log(priority, tag, t, supplier.get() + message);
}

public static IStringSupplier threadName() {
	return () -> "Thread:" + Thread.currentThread().getName();
}
	
public static IStringSupplier string(String givenString) {
	return () -> givenString;
}

public static IStringSupplier concatString(IStringSupplier... suppliers) {
	return () -> Arrays.stream(suppliers).map(IStringSupplier::get).collect(Collectors.joining());
}
```
```
FLog.init(
        prefixTag(
                prefixMessage(
                        logcat(),
                        concatString(threadName(), string(" >>> "))
                ),
                string("MyApp-")
        )
);
```

If you want to avoid lots of nested calls, you can use `java.util.Optional` or `RxJava` make it into chaining:
```
FLog.init(
        Optional.of(Loggers.logcat())
                .map(logger -> prefixMessage(logger, concat(threadName(), string(" >>> "))))
                .map(logger -> prefixTag(logger, string("MyApp-")))
                .get()
);
```


The above examples are just the tip of an iceberg, we can do more powerful works by combination, such as
print things only at WARN and ERROR level,
convert object to formatted string,
write log to file asynchronously,
save log to database asynchronously,
make log like [Orhan Obut Logger](https://github.com/orhanobut/logger)'s style,
and so on.

All these have implemented in demo, please check it.

# License
Copyright 2017 AvatarQing

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.