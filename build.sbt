name := "ifcfg-api"

version := "1.0-SNAPSHOT"

resolvers += "wasted.io/repo" at "http://repo.wasted.io/mvn"

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/"

resolvers += Resolver.file("Local repo", file(System.getProperty("user.home") + "/.ivy2/local"))(Resolver.ivyStylePatterns)

resolvers += "sonatype.repo" at "https://oss.sonatype.org/content/groups/public"


libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "io.wasted" %% "wasted-util" % "0.5.0-SNAPSHOT",
  "com.twitter" %% "util-collection" % "6.3.6",
  "com.joshrendek" %% "scala-bgp" % "0.0.3"
)

play.Project.playScalaSettings
