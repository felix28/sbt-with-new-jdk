ThisBuild / name := "dmp-spark-jobs"

ThisBuild / version := "1.0"

ThisBuild / scalaVersion := "2.12.10"

ThisBuild / organization := "mediajel.spark"

javacOptions ++= Seq("-source", "1.11", "-target", "1.11", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "11")
    sys.error("Java 11 is required for this project.")
}

scalacOptions += "-Ypartial-unification"
autoScalaLibrary := true

val analyticsSdk = "com.snowplowanalytics" %% "snowplow-scala-analytics-sdk" % "0.4.0"
val circeJava8 = "io.circe" %% "circe-java8" % "0.11.1"
val circeCore = "io.circe" %% "circe-core" % "0.11.1"
val circeGeneric = "io.circe" %% "circe-generic" % "0.11.1"
val circeParse = "io.circe" %% "circe-parser" % "0.11.1"
val cats = "org.typelevel" %% "cats-core" % "1.6.0"

val sparkCore = "org.apache.spark" %% "spark-core" % "3.0.0" % "provided"

val sparkSql = "org.apache.spark" %% "spark-sql" % "3.0.0" % "provided"
val sparkStreaming = "org.apache.spark" %% "spark-streaming" % "3.0.0" % "provided"

val scalapbJson = "com.thesamet.scalapb" %% "scalapb-json4s" % "0.9.1"

val aws = "com.amazonaws" % "aws-java-sdk-core" % "1.11.375"
val awsS3 = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.375"
val awsSts = "com.amazonaws" % "aws-java-sdk-sts" % "1.11.375"
val hadoopAws = "org.apache.hadoop" % "hadoop-aws" % "3.2.0"
val mysql = "mysql" % "mysql-connector-java" % "5.1.47"
val httpClient = "org.apache.httpcomponents" % "httpclient" % "4.3.6"
val httpCore = "org.apache.httpcomponents" % "httpcore" % "4.3.3"
val gcsConnectorShaded = "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.0.0" artifacts(Artifact("gcs-connector","hadoop3-2.0.0-shaded","jar"))
val gcsConnector = "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.0.0" exclude("com.google.guava","guava") exclude("javax.jms", "jms") exclude("com.sun.jdmk", "jmxtools") exclude("com.sun.jmx", "jmxri") exclude("org.slf4j", "slf4j-simple")

val guava = "com.google.guava" % "guava" % "28.1-jre" % "provided"
val guavaFailureAccess = "com.google.guava" % "failureaccess" % "1.0.1"

// val sparkTestingBase = "com.holdenkarau" %% "spark-testing-base" % "3.0.0" % "test"
val jacksonTest = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"
val snowflakeConnector = "net.snowflake" %% "spark-snowflake" % "2.8.1-spark_3.0"
val snowflakeJdbc = "net.snowflake" % "snowflake-jdbc" % "3.12.8"
val snowflakeIngest = "net.snowflake" % "snowflake-ingest-sdk" % "0.9.9"

val jodaTime = "joda-time" % "joda-time" % "2.10.4"
val geo = "com.github.davidmoten" % "geo" % "0.7.6"
val scopt = "com.github.scopt" %% "scopt" % "3.7.1"
val scalaHttp = "org.scalaj" %% "scalaj-http" % "2.3.0"

libraryDependencies ++= Seq(
  analyticsSdk,
  cats,
  circeCore,
  circeJava8,
  circeGeneric,
  circeParse,
  sparkCore,
  sparkSql,
  sparkStreaming,
  aws,
  awsS3,
  awsSts,
  hadoopAws,
  mysql,
  scalapbJson,
  //sparkTestingBase,
  jacksonTest,
  jodaTime,
  geo,
  scopt,
  jacksonTest,
  httpClient,
  httpCore,
  snowflakeConnector,
  snowflakeJdbc,
  gcsConnector,
  gcsConnectorShaded,
  guava,
  guavaFailureAccess,
  scalaHttp
)

// change to v2 of protobuf protocol
PB.protocVersion := "-v261"

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.protobuf.*" -> "shadedgoogle.@1").inAll
  //ShadeRule.rename("org.json4s.*" -> "shadedjson4s.@1").inAll
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

fork in Test := true

javaOptions ++= Seq("-Xms1024M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")

parallelExecution in Test := false