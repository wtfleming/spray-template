import AssemblyKeys._
import com.typesafe.startscript.StartScriptPlugin

organization  := "com.example"

version       := "0.1-SNAPSHOT"

scalaVersion  := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-encoding", "utf8")

resolvers ++= Seq(
  "spray" at "http://repo.spray.io/"
)

libraryDependencies ++= {
  val akkaVersion = "2.2.3"
  val sprayVersion = "1.2.0"
  Seq(
    "io.spray"            %   "spray-can"       % sprayVersion,
    "io.spray"            %   "spray-routing"   % sprayVersion,
    "io.spray"            %   "spray-testkit"   % sprayVersion,
    "io.spray"            %   "spray-httpx"     % sprayVersion,
    "io.spray"            %%  "spray-json"      % "1.2.5",
    "com.typesafe.akka"   %%  "akka-actor"      % akkaVersion,
    "com.typesafe.akka"   %%  "akka-slf4j"      % akkaVersion,
    "com.typesafe.akka"   %%  "akka-testkit"    % akkaVersion,
    "org.scalatest"       %%  "scalatest"       % "2.0"         % "test",
    "ch.qos.logback"      %   "logback-classic" % "1.0.13"
  )
}

// Typesafe Console
atmosSettings

// sbt-revolver settings
seq(Revolver.settings: _*)

// Assembly settings
mainClass in Global := Some("com.example.Boot")

jarName in assembly := "spray-template.jar"

assemblySettings

// StartScript settings
seq(StartScriptPlugin.startScriptForClassesSettings: _*)
