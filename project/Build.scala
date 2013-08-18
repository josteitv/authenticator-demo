import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "authenticator-demo"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
		  "org.jasypt" % "jasypt" % "1.9.0",
		  "commons-codec" % "commons-codec" % "1.7",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
