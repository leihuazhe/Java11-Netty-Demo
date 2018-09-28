# Java11-Netty-Demo

Java11 基于maven构建的简单的服务端客户端分离模块调用的例子

## Java 11 
> 从 Java9 开始引入了模块化的概念。使用Java11 也需要以模块化的方式进行项目的构建和编译。

### `Maven` 项目支持
> 为支持 Java9 之后的模块化， Maven 很多插件需要升级到比较新的版本。

#### 编译器插件

为了能够使用JDK 9 的模块系统 maven-compiler-plugin 版本3.6.1或更高版本是必需的。
```xml
<plugin>
     <groupId>org.apache.maven.plugins</groupId>
     <artifactId>maven-compiler-plugin</artifactId>
     <version>3.8.0</version>
     <configuration>
           <source>11</source>
           <target>11</target>
           <encoding>utf8</encoding>
     </configuration>
</plugin>

```

#### 工具链插件
这或多或少是可选的，但是我强烈建议您使用它。
由于现在Java版本更新很快，但是大部分项目还是基于 Java8 甚至更低版本。不适宜更改项目所有的环境变量，并将其指向JDK11的主目录。
使用 maven-toolchains-plugin使您能够轻松地使用各种环境。

创建$HOME/.m2/toolchains.xml（或 %USERPROFILE%\.m2\toolchains.xml在Windows上），如果你还没有的话。
```xml
<toolchains>
  <toolchain>
    <type>jdk</type>
    <provides>
      <version>11</version>
      <vendor>oracle</vendor>
    </provides>
    <configuration>
      <!-- Change path to JDK9 -->
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk-11.jdk/Contents/Home</jdkHome>
    </configuration>
</toolchain>

<toolchain>
    <type>jdk</type>
    <provides>
      <version>8</version>
      <vendor>oracle</vendor>
    </provides>
    <configuration>
      <jdkHome>/Library/Java/JavaVirtualMachines/jdk-8.jdk/Contents/Home</jdkHome>
    </configuration>
  </toolchain>
</toolchains>

```

- 注意：将配置文件中 <jdkHome>  更改为实际的JDK安装HOME。


##### 项目主POM 文件 添加 工具链插件
```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-toolchains-plugin</artifactId>
  <version>1.1</version>
  <configuration>
    <toolchains>
        <jdk>
            <version>11</version>
            <vendor>oracle</vendor>
        </jdk>
    </toolchains>
  </configuration>
  <executions>
    <execution>
          <goals>
            <goal>toolchain</goal>
        </goals>
    </execution>
  </executions>
</plugin>

```


### 启用Java 11 （9）语言支持

```xml
<properties>
  <maven.compiler.release>11</maven.compiler.release>
  <maven.compiler.source>11</maven.compiler.source>
  <maven.compiler.target>11</maven.compiler.target>
</properties>

```

属性 maven.compiler.release直接映射到该--release标志javac
另外两个属性只对IntelliJ有必要 ，用来了解源码兼容性。

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>${maven-compiler-plugin.version}</version>
  <!--
    Fix breaking change introduced by JDK-8178012: Finish removal of -Xmodule
    Reference:  http://bugs.java.com/bugdatabase/view_bug.do?bug_id=8178012
  -->
  <executions>
    <execution>
      <id>default-testCompile</id>
      <phase>test-compile</phase>
      <goals>
        <goal>testCompile</goal>
      </goals>
      <configuration>
        <skip>true</skip>
      </configuration>
    </execution>
  </executions>
  <configuration>
    <showWarnings>true</showWarnings>
    <showDeprecation>true</showDeprecation>
  </configuration>
</plugin>
```


### Netty with  java 11

项目结构，包含 4 大模块，分别是：
- hello-api
- hello-common
- hello-client
- hello-service

每个模块src根目录下都有一个 `module-info.java` 文件用来定义模块

```

├── hello-api
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   ├── com
│   │   │   │   │   └── maple
│   │   │   │   │       └── hello
│   │   │   │   │           ├── HelloRequest.java
│   │   │   │   │           └── HelloResponse.java
│   │   │   │   └── module-info.java
│   │   │   └── resources
│   
├── hello-client
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   ├── com
│   │   │   │   │   └── maple
│   │   │   │   │       └── hello
│   │   │   │   │           └── client
│   │   │   │   │               ├── AppClient.java
│   │   │   │   │               ├── Main.java
│   │   │   │   │               ├── netty
│   │   │   │   │               │   ├── NettyClient.java
│   │   │   │   │               │   └── handler
│   │   │   │   │               │       ├── RpcClientHandler.java
│   │   │   │   │               │       ├── RpcClientMsgDecoder.java
│   │   │   │   │               │       └── RpcClientMsgEncoder.java
│   │   │   │   │               └── service
│   │   │   │   │                   └── HelloClient.java
│   │   │   │   └── module-info.java
│   │   │   └── resources
│   │   │       └── logback.xml
│   │   └── test
│   │       └── java
│   
├── hello-common
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   ├── com
│   │   │   │   │   └── maple
│   │   │   │   │       └── hello
│   │   │   │   │           └── common
│   │   │   │   │               ├── Constants.java
│   │   │   │   │               ├── DumpUtil.java
│   │   │   │   │               ├── RpcException.java
│   │   │   │   │               └── netty
│   │   │   │   │                   └── RpcFrameDecoder.java
│   │   │   │   └── module-info.java
│   │   │   └── resources
│   └── 
├── hello-service
│   ├── hello-service.iml
│   ├── pom.xml
│   ├── src
│   │   ├── main
│   │   │   ├── java
│   │   │   │   ├── com
│   │   │   │   │   └── maple
│   │   │   │   │       ├── AppServer.java
│   │   │   │   │       ├── hello
│   │   │   │   │       │   └── service
│   │   │   │   │       │       ├── HelloService.java
│   │   │   │   │       │       └── Person.java
│   │   │   │   │       └── netty
│   │   │   │   │           ├── NettySimpleServer.java
│   │   │   │   │           └── handler
│   │   │   │   │               ├── RpcLogHandler.java
│   │   │   │   │               ├── RpcMsgDecoder.java
│   │   │   │   │               ├── RpcMsgEncoder.java
│   │   │   │   │               └── ServerHandler.java
│   │   │   │   └── module-info.java
│   │   │   └── resources
│   │   │       └── logback.xml

```

DEBUG 错误1:

- Reflective setAccessible(true) disabled

TODO

## 迁移注意事项
### 1. JavaEE 模块被移除
Java11 移除了 JavaEE 模块,所以很多诸如 javax JAXB 等已经被移除。
如果旧版本的项目有依赖 Javaee的组件，需要单独加入 javaee-api
```xml
<dependency>
    <groupId>javax</groupId>
    <artifactId>javaee-api</artifactId>
    <version>8.0</version>
    
</dependency>
```

### 2.模块化 api
exports 和 exports to 指令
exports 指令用于指定一个模块中哪些包对外是可访问的，而 exports…to 指令则用来限定哪些模块可以访问导出类，允许开发者通过逗号分隔的列表指定哪些模块及模块的哪些代码可以访问导出的包，这种方式也称为限定导出(qualified export)。

use 指令
use 指令用于指定一个模块所使用的服务，使模块成为服务的消费者，服务其实就是一个实现了某个接口或抽象类的对象。

provides…with 指令
该指令用于说明模块提供了某个服务的实现，因此模块也称为服务提供者。provides 后面跟接口名或抽象类名，与 use 指令后的名称一致，with 后面跟实现类该接口或抽象类的类名。

open, opens, opens…to 指令
在 Java 9 之前，我们可以通过反射技术来获取某个包下所有的类及其内部乘员的信息，即使是 private 类型我们也能获取到，所以类信息并不是真的与外界完全隔离的。而模块系统的主要目标之一就是实现强封装，默认情况下，除非显式地导出或声明某个类为 public 类型，那么模块中的类对外部都是不可见的，模块化要求我们对外部模块应最小限度地暴露包的范围。open 相关的指令就是用来限制在运行时哪些类可以被反射技术探测到。

首先我们先看 opens 指令，语法如下：
opens package
opens 指令用于指定某个包下所有的 public 类都只能在运行时可被别的模块进行反射，并且该包下的所有的类及其乘员都可以通过反射进行访问。

opens…to 指令，语法如下：
opens package to modules
该指令用于指定某些特定的模块才能在运行时对该模块下特定包下的 public 类进行反射操作，to 后面跟逗号分隔的模块名称。

open 指令，语法如下：
open module moduleName{
}
该指令用于指定外部模块可以对该模块下所有的类在运行时进行反射操作。







