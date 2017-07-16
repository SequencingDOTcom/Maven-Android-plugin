# Gradle and Maven plugin for implementing Sequencing.com's OAuth2 and File Selector in Java applications

This repo contains a Gradle and Maven plugin that will quickly add Sequencing.com's OAuth2 authentication and File Selector to your Java application. This will allow your app to securely connect to and use Sequencing.com's APIs and [App Chains](https://sequencing.com/app-chains/). 

For OAuth flow reference see [here](https://github.com/SequencingDOTcom/OAuth2-code-with-demo).

This module allows quick integration of Sequencing.com oAuth support to Gradle and Maven based applications
* Java based web applications ([plain Servlet example](https://github.com/SequencingDOTcom/OAuth2-code-with-demo/tree/master/java-servlet), [Spring and Spring Boot example](https://github.com/SequencingDOTcom/OAuth2-code-with-demo/tree/master/java-spring))
* Android applications ([example](https://github.com/SequencingDOTcom/OAuth2-code-with-demo/tree/master/android))

Contents
=========================================
* Integration with a Java application
* Resources
* Maintainers
* Contribute

Integration with the Java application
======================================

Integration as simple as adding following snippet to your pom.xml if you use Maven for dependency management

```
<dependency>
	<groupId>com.sequencing</groupId>
	<artifactId>oauth2-core</artifactId>
	<version>1.9</version>
</dependency>
```

or following line to the "dependencies" section of your build.gradle

```
compile 'com.sequencing:oauth2-core:1.9'
```

Resources
======================================
* [App chains](https://sequencing.com/app-chains)
* [File selector code](https://github.com/SequencingDOTcom/File-Selector-code)
* [Developer center](https://sequencing.com/developer-center)
* [Developer Documentation](https://sequencing.com/developer-documentation/)

Maintainers
======================================
This repo is actively maintained by [Sequencing.com](https://sequencing.com/). Email the Sequencing.com bioinformatics team at gittaca@sequencing.com if you require any more information or just to say hola.

Contribute
======================================
We encourage you to passionately fork us. If interested in updating the master branch, please send us a pull request. If the changes contribute positively, we'll let it ride.
