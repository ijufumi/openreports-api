// Because the latest flyway-sbt uses the flyway version under 7.4.0
libraryDependencies += "org.flywaydb" % "flyway-core" % "8.5.13"

addSbtPlugin("com.earldouglas" %% "xsbt-web-plugin" % "4.2.4")
addSbtPlugin("com.eed3si9n" %% "sbt-assembly" % "1.2.0")
addSbtPlugin("org.scalameta" %% "sbt-scalafmt" % "2.4.6")
addSbtPlugin("org.scala-sbt" %% "sbt-contraband" % "0.5.3")
addSbtPlugin("org.scalameta" %% "sbt-scalafmt" % "2.4.6")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "7.4.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.3")
