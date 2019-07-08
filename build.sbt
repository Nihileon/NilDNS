name := "NilDNS"

version := "0.1"

scalaVersion := "2.12.5"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.4"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.28"

libraryDependencies += "org.scalaz.stream" %% "scalaz-stream" % "0.8.6a"

libraryDependencies += "org.scalaz" %% "scalaz-concurrent" % "7.3.0-M27"
