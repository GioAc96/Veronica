<p align="center">
  <img src="/docs/static/images/logo/logo-dark.svg" width="40%" align="center">
  <br>
  <br>
  <i>A fast, elegant and versatile Java backend web framework.</i>
  <br>
  <br>
</p>
<h2>State of development</h2>
<p>Veronica is still in the early stages of its development and is missing proper testing.
<p>You can find a work in progress version of the documentation <a href="https://veronica-gioac96.readme.io/">here</a>.</p>
<a href="https://jitpack.io/#GioAc96/Veronica"><img alt="Release" src="https://jitpack.io/v/GioAc96/Veronica.svg"></a>
<hr>
<p align="center">Developed with ❤️ by Giorgio Acquati.<br><a href="https://acquati.dev/">https://acquati.dev/</a></p>

# 💣Version 1.0 Features
This is the first production ready release of Veronica. Here's what has changed from version 0.14-preview:

## 🗃️ Sessions

Veronica 1.0 adds support for Server-Side session storage. Currently, the framework provides two different implementations:

### 💨 Application Session Storage
This implementation associates each session with a unique id stored inside of HTTP Cookies. The session data is stored by the Veronica Application inside of a HashMap. This provides very fast session data access, at the expense of server memory usage. This is ideal for small applications or fast prototyping.

### <img src="https://www.vectorlogo.zone/logos/redis/redis-icon.svg" height="26px"> Redis Session Storage
This implementation stores session data inside of a Redis database. It makes use of the [Jedis library](https://github.com/redis/jedis) to communicate with the database.

## ✅ Validation Overhaul

The validation features of Veronica have been re-written from scratch, to match the coding standards used in the most recent versions of the framework.

## ⚙️ New pipeline

The pipeline structure has been greatly simplified, getting rid of obscure terminology used to describe its components.

## <img src="https://graphql.org/img/logo.svg" height="30px"> Support for GraphQL

Veronica now supports serving GraphQL api endpoints.

## 📦 Modules
Veronica is now split into several modules. This allows developers to include only the functionalities that they need need in their project, minimizing the impact that Veronica has on application architecture. Here is a list of the current modules that shipped with this version of Veronica:
* `core`
This module contains the main components of a Veronica application. With this module alone, you have all you need to build a fully functional Veronica application.
* `routing`
This module contains the implementation of the Veronica radix router and its related components.
* `pipeline`
This module contains the implementation of the pipeline request handler.
* `auth`
This module contains general authentication interfaces/classes. Currently, the only implementation of the authentication feature is provided by the `http-basic-auth` module.
* `http-basic-auth`
Fully featured implementation of the HTTP Basic Authentication standard
* `session`
This module contains general interfaces and classes for the Server Side Sessions feature.
* `cookie-session`
This module contains common classes for Server Side Sessions implementations that store session ids in HTTP Cookies.
* `redis-session`
This is an implementation of the Server Side Sessions feature using a Redis database to store session data.
* `application-session`
This is an implementation of the Server Side Sessions feature storing session data in the application.
* `file-server`
This module contains the implementation of a simple static file server.
* `graphql`
This module contains a simple implementation of a GraphQL endpoint in Veronica.
* `validation`
This module contains all classes related to request validation features.
* `samples`
This is a special module containing Veronica sample applications

# 🚀  Installation

Veronica is distributed via [Jitpack](https://jitpack.io/). Here are the installation instructions for Veronica 1.0

Gradle:
```groovy
repositories {
	...
	maven { url 'https://jitpack.io' }
}
dependencies {
        ...

         // Veronica 1.0 https://acquati.dev/veronica
	 implementation 'com.github.GioAc96:Veronica:core:1.0'
	 // implementation 'com.github.GioAc96:Veronica:routing:1.0'
	 // implementation 'com.github.GioAc96:Veronica:pipeline:1.0'
	 // implementation 'com.github.GioAc96:Veronica:file-server:1.0'
	 // implementation 'com.github.GioAc96:Veronica:auth:1.0'
	 // implementation 'com.github.GioAc96:Veronica:http-basic-auth:1.0'
	 // implementation 'com.github.GioAc96:Veronica:session:1.0'
	 // implementation 'com.github.GioAc96:Veronica:cookie-session:1.0'
	 // implementation 'com.github.GioAc96:Veronica:redis-session:1.0'
	 // implementation 'com.github.GioAc96:Veronica:application-session:1.0'
	 // implementation 'com.github.GioAc96:Veronica:graphql:1.0'
	 // implementation 'com.github.GioAc96:Veronica:validation:1.0'
}
```

Maven:
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>core</artifactId>
        <version>1.0</version>
</dependency>
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>routing</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>pipeline</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>file-server</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>auth</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>http-basic-auth</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>session</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>cookie-session</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>redis-session</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>application-session</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>graphql</artifactId>
        <version>1.0</version>
</dependency!-->
<!--dependency>
        <groupId>com.github.GioAc96.Veronica</groupId>
        <artifactId>validation</artifactId>
        <version>1.0</version>
</dependency!-->
```
