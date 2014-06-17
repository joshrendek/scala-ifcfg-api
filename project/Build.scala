import sbt._

object MyBuild extends Build {

  lazy val root = Project("root", file(".")) dependsOn(bgpLib)
  lazy val bgpLib = RootProject(uri("git://github.com/joshrendek/scala-bgp.git"))

}
