[![Release](https://jitpack.io/v/AvatarQing/FLog.svg)](https://jitpack.io/#AvatarQing/FLog)

[English README](README-en.md)

# FLog
一个基于函数组合的Android日志框架，拥有极简的结构和极高的灵活性、扩展性

# 下载
在根目录下的build.gradle中添加jitpack.io的maven地址
```
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

在模块的build.gradle中添加依赖
```
dependencies {
    // 仅包含ILogger和FLog
    implementation 'com.github.AvatarQing.FLog:flog-core:0.1.0'
	
    // 包含了一些常用的ILogger的实现
    implementation 'com.github.AvatarQing.FLog:flog-loggers:0.1.0'
}
```

# 使用
1. 创建一个`ILogger`实例
2. 调用 `FLog.init(ILogger logger)` 进行初始化
3. 再调用 `FLog`的各个静态方法来打印日志

详情请查阅示例代码

# 详解
整个库由两个核心类构成：`ILogger`和`FLog`。

`ILogger`是一个函数式接口（使用`@FunctionalInterface`注解标记），只有一个方法：  
`public void log(int priority, String tag, Throwable t, Object message);`

`FLog`对`ILogger`又做了一层封装，内部维持一个静态的`ILogger`的引用，提供多个辅助方法，例如`v()`、`d()`、`i()`、`w()`、`e()`等等，以便输出不同级别的日志。

那么如何实现各种五花八门的日志输出需求呢？我们可以创建各种`ILogger`的实现类，每个实现类只做一种操作（单一职责），再像搭积木一样将它们组合起来合并成一个`ILogger`传给`FLog`即可。

输出日志到Logcat：
```
public class LogcatLogger implements ILogger {
    @Override
    public void log(int priority, String tag, Throwable t, Object message) {
        Log.println(priority, tag, String.valueOf(message));
    }
}
```

在消息末尾追加信息（如堆栈或线程信息）：
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

在标签前面添加前缀:
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

以上代码过于繁琐，我们可以用Java8的lambda表达式精简代码：
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

然后我们就可以用简明的方式进行初始化:
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

调用日志输出方法：
```
FLog.d("test", "Hello World");
```

在Logcat中的显示效果如下：
```
11-16 09:56:34.533 9924-9924/com.avatarqing.tools.log.demo D/MyApp-test: Hello World >>> Thread:main
```

这样做看似啰嗦了，但我们只要改变组合的方式，就可以以最小的代码改动适应千变万化的需求，有效的遵循了开闭原则。

例如某一天突然想在消息的前面打印线程信息（形如“Thread:main >>> Hello World”），我们只需要：
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

如果想避免大量的嵌套调用，可使用`java.util.Optional`或`RxJava`的链式调用：
```
FLog.init(
        Optional.of(Loggers.logcat())
                .map(logger -> prefixMessage(logger, concat(threadName(), string(" >>> "))))
                .map(logger -> prefixTag(logger, string("MyApp-")))
                .get()
);
```


以上示例只是冰山一角，通过定义基本的单元操作，我们可以组合出各种强大的功能，例如
只输出警告和错误级别的日志、
将对象转换为特定格式的字符串、
将日志异步写入文件、
将日志异步写入数据库、
实现[Orhan Obut Logger](https://github.com/orhanobut/logger)风格的日志打印效果
等等，
这些都在示例代码里实现了，详细的用法请参考示例代码。

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