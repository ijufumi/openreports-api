addSbtPlugin("com.earldouglas" %% "xsbt-web-plugin" % "4.2.4")
addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "2.1.1")
addSbtPlugin("org.scalameta" %% "sbt-scalafmt" % "2.5.6")
addSbtPlugin("com.github.sbt" % "flyway-sbt" % "11.11.0")
libraryDependencies += "org.flywaydb" % "flyway-database-postgresql" % "11.11.0"
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.10.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.10")
