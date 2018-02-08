name := "UserService"

version := "1.0"

lazy val `usermicroservice` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

fork in run := true
javaOptions in run += "-Dhttp.port=9001"

libraryDependencies ++= Seq(
  evolutions ,
  ehcache ,
  ws ,
  specs2 % Test ,
  guice ,
  "com.typesafe.play" %% "play-slick" % "3.0.0" ,
  "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0" ,
  "com.h2database" % "h2" % "1.4.192" ,
  "org.mindrot"  % "jbcrypt"   % "0.3m"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

