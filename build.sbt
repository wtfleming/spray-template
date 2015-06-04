organization  := "com.example"

version       := "0.1-SNAPSHOT"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

resolvers ++= Seq(
  "spray" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"       % sprayVersion,
    "io.spray"            %%  "spray-routing"   % sprayVersion,
    "io.spray"            %%  "spray-testkit"   % sprayVersion,
    "io.spray"            %%  "spray-httpx"     % sprayVersion,
    "io.spray"            %%  "spray-json"      % "1.3.2",
    "com.typesafe.akka"   %%  "akka-actor"      % akkaVersion,
    "com.typesafe.akka"   %%  "akka-slf4j"      % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"    % akkaVersion,
    "org.scalatest"       %%  "scalatest"       % "2.2.4"         % "test",
    "ch.qos.logback"      %   "logback-classic" % "1.1.3"
  )
}

Revolver.settings

// Assembly settings
mainClass in Global := Some("com.example.Boot")

jarName in assembly := "spray-template.jar"


// StartScript settings
import com.typesafe.sbt.SbtStartScript
seq(SbtStartScript.startScriptForClassesSettings: _*)
