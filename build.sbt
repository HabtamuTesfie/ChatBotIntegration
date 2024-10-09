name := """ChatBotIntegration"""
organization := "EDUTERIUM"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
    guice,// Guice for DI
    javaWs, // To use WS
    "org.postgresql" % "postgresql" % "42.6.0",
    "org.projectlombok" % "lombok" % "1.18.28" % Provided,
    "org.hibernate" % "hibernate-core" % "5.6.9.Final",
    "com.zaxxer" % "HikariCP" % "5.0.1",  // Connection Pooling

    "com.typesafe.play" %% "play-ahc-ws" % "2.9.5",
//    "com.typesafe.play" %% "play-ahc-ws" % "2.9.4",

//    // Akka dependencies
    "com.typesafe.akka" %% "akka-actor" % "2.6.20",
    "com.typesafe.akka" %% "akka-stream" % "2.6.20",

    // Play JPA and Hibernate Validator
    "com.typesafe.play" % "play-java-jpa_2.13" % "2.8.18",

    // Hibernate Validator and javax validation API
    "org.hibernate.validator" % "hibernate-validator" % "6.2.0.Final",
    "javax.validation" % "validation-api" % "2.0.1.Final",

    // Logging
    "org.slf4j" % "slf4j-api" % "1.7.36",
    "ch.qos.logback" % "logback-classic" % "1.2.11",

    // Testing
    "org.scalatestplus" %% "junit-4-13" % "3.2.16.0",
    "org.mockito" % "mockito-core" % "5.5.0"
)



