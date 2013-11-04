name := "ifcfg-api"

version := "1.0-SNAPSHOT"

resolvers += "wasted.io/repo" at "http://repo.wasted.io/mvn"

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"

resolvers += Resolver.file("Local repo", file(System.getProperty("user.home") + "/.ivy2/local"))(Resolver.ivyStylePatterns)

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "com.joshrendek" %% "scala-bgp" % "0.0.1",
  "io.wasted" %% "wasted-util" % "0.5.0-SNAPSHOT"
)

play.Project.playScalaSettings
