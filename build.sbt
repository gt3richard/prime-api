
name := "prime-api"

version := "0.1"

scalaVersion := "2.12.5"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

def akka(module: String): ModuleID    = "com.typesafe.akka" %% s"akka-$module"   % "2.5.10"
def specs2m(module: String): ModuleID = "org.specs2"        %% s"specs2-$module" % "4.0.2"

libraryDependencies ++= Seq(
  guice,
  "net.codingwell" %% "scala-guice" % "4.1.0",
  // Akka
  akka("actor"),
  akka("stream"),
  akka("slf4j"),
  // Slick
  "com.typesafe.slick" %% "slick"                 % "3.2.1",
  "com.typesafe.slick" %% "slick-hikaricp"        % "3.2.1",
  "com.typesafe.play"  %% "play-slick"            % "3.0.1",
  "com.typesafe.play"  %% "play-slick-evolutions" % "3.0.1",
  "com.h2database"     % "h2"                     % "1.4.196",
  // Test
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,
  specs2m("core")          % Test,
  specs2m("junit")         % Test,
  specs2m("mock")          % Test,
  specs2m("scalacheck")    % Test,
  "org.scalacheck"         %% "scalacheck" % "1.13.5" % Test
)