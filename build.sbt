import JavaScriptBuild._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import play.sbt.PlayImport.PlayKeys.playDefaultPort
import sbt.Keys.testOptions
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, integrationTestSettings}

val appName = "tracking-consent-frontend"

lazy val unitTestSettings =
  inConfig(Test)(Defaults.testTasks) ++
    Seq(
      testOptions in Test := Seq(Tests.Filter(_ startsWith "unit")),
      addTestReportOption(Test, "test-reports")
    )

lazy val IntegrationTest         = config("it") extend Test
lazy val integrationTestSettings =
  inConfig(IntegrationTest)(Defaults.testTasks) ++
    Seq(
      // The following is needed due to https://stackoverflow.com/questions/24791992/assets-are-not-loaded-in-functional-test-mode
      (managedClasspath in IntegrationTest) += (packageBin in Assets).value,
      (test in IntegrationTest) := (test in IntegrationTest).dependsOn(npmBuild).value,
      (testOptions in IntegrationTest) := Seq(Tests.Filter(_ startsWith "it")),
      addTestReportOption(IntegrationTest, "it-test-reports")
    )

lazy val AcceptanceTest         = config("acceptance") extend Test
lazy val acceptanceTestSettings =
  inConfig(AcceptanceTest)(Defaults.testTasks) ++
    Seq(
      // The following is needed to preserve the -Dbrowser option to the HMRC webdriver factory library
      fork in AcceptanceTest := false,
      // The following is needed due to https://stackoverflow.com/questions/24791992/assets-are-not-loaded-in-functional-test-mode
      (managedClasspath in AcceptanceTest) += (packageBin in Assets).value,
      (test in AcceptanceTest) := (test in AcceptanceTest).dependsOn(npmBuild).value,
      (testOptions in AcceptanceTest) := Seq(Tests.Filter(_ startsWith "acceptance")),
      addTestReportOption(AcceptanceTest, "acceptance-test-reports")
    )

lazy val ZapTest         = config("zap") extend Test
lazy val zapTestSettings =
  inConfig(ZapTest)(Defaults.testTasks) ++
    Seq(
      fork in ZapTest := false,
      testOptions in ZapTest := Seq(Tests.Filter(_ startsWith "zap"))
    )

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) // Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .configs(AcceptanceTest, IntegrationTest, ZapTest)
  .settings(
    majorVersion := 0,
    scalaVersion := "2.12.11",
    playDefaultPort := 12345,
    resolvers += Resolver.jcenterRepo,
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    TwirlKeys.templateImports ++= Seq(
      "views.html.helper.CSPNonce",
      "uk.gov.hmrc.trackingconsentfrontend.config.AppConfig",
      "uk.gov.hmrc.trackingconsentfrontend.views.html.components._",
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.govukfrontend.views.html.helpers._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._"
    ),
    PlayKeys.playRunHooks += Webpack(javaScriptDirectory.value),
    PlayKeys.devSettings ++= Seq("metrics.enabled" -> "false"),
    pipelineStages in Assets := Seq(gzip),
    acceptanceTestSettings,
    unitTestSettings,
    zapTestSettings,
    integrationTestSettings,
    publishingSettings,
    javaScriptSettings,
    // ***************
    // Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
    scalacOptions += "-P:silencer:pathFilters=views;routes",
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.6.0" cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % "1.6.0" % Provided cross CrossVersion.full
    )
    // ***************
  )
