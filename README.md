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







