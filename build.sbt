name := "breast-cancer"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-sql" % "2.4.0",
    "org.apache.spark" %% "spark-mllib" % "2.4.0",
    "com.arangodb" %% "arangodb-spark-connector" % "1.0.11",
    "com.typesafe" % "config" % "1.3.4"
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"