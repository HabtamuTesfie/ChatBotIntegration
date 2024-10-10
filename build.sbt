name := """ChatBotIntegration"""
organization := "EDUTERIUM"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
    guice,
    javaWs,
    "org.postgresql" % "postgresql" % "42.6.0",
    "org.projectlombok" % "lombok" % "1.18.28" % Provided,
    "org.hibernate" % "hibernate-core" % "5.6.9.Final",
    "org.hibernate" % "hibernate-hikaricp" % "5.6.9.Final",
    "com.typesafe.play" %% "play-ahc-ws" % "2.9.5",
    "com.typesafe.akka" %% "akka-actor" % "2.6.20",
    "com.typesafe.akka" %% "akka-stream" % "2.6.20",
    "com.typesafe.play" % "play-java-jpa_2.13" % "2.8.18",
    "org.hibernate.validator" % "hibernate-validator" % "6.2.0.Final",
    "javax.validation" % "validation-api" % "2.0.1.Final",
    "org.slf4j" % "slf4j-api" % "1.7.36",
    "ch.qos.logback" % "logback-classic" % "1.2.11",
    "org.scalatestplus" %% "junit-4-13" % "3.2.16.0",
    "org.mockito" % "mockito-core" % "5.5.0"
)



