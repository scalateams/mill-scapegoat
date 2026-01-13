package org.scalateams.mill.scapegoat

import mill.*
import mill.scalalib.*
import org.scalateams.mill.scapegoat.ScapegoatModule.{PluginArtifactId, PluginGroupId}

trait ScapegoatModule extends ScalaModule {

  /** Whether to output results to console. */
  def scapegoatConsoleOutput: T[Boolean] = Task { true }

  /** List of disabled inspections. */
  def scapegoatDisabledInspections: T[Seq[String]] = Task { Seq.empty }

  /** Whether scapegoat is enabled. */
  def scapegoatEnabled: T[Boolean] = Task { true }

  /** List of enabled inspections. If empty, all inspections are enabled. */
  def scapegoatEnabledInspections: T[Seq[String]] = Task { Seq.empty }

  /** List of file patterns to ignore. */
  def scapegoatIgnoredFiles: T[Seq[String]] = Task { Seq.empty }

  /** Minimal inspection level to be displayed in the reports. */
  def scapegoatMinimalInspectionLevel: T[String] = Task { "info" }

  /** Output directory for scapegoat reports. */
  def scapegoatOutput: T[os.Path] = Task.dest

  /** Report formats to generate (xml, html, scalastyle, markdown, all). */
  def scapegoatReportFormats: T[Seq[String]] = Task { Seq("all") }

  /** Scalac options required to enable scapegoat. */
  def scapegoatScalacOptions: T[Seq[String]] = Task {
    if (!scapegoatEnabled()) Seq.empty
    else {
      val pluginClasspathOpt =
        scapegoatPluginClasspath().map(_.path).find(_.segments.contains(s"${PluginArtifactId}_${scalaVersion()}"))
      pluginClasspathOpt match {
        case None            => Task.fail(s"Artifact `$PluginArtifactId` was not found on the classpath!")
        case Some(classpath) =>

          val baseOptions = Seq(
            s"-Xplugin:${classpath.toIO.getAbsolutePath()}",
            s"-P:scapegoat:dataDir:${scapegoatOutput().toIO.getAbsolutePath()}",
            s"-P:scapegoat:consoleOutput:${scapegoatConsoleOutput()}",
            s"-P:scapegoat:verbose:${scapegoatVerbose()}",
            s"-P:scapegoat:sourcePrefix:${scapegoatSourcePrefix()}",
            s"-P:scapegoat:minimalWarnLevel:${scapegoatMinimalInspectionLevel()}",
          )

          val disabledInspections = {
            val inspections = scapegoatDisabledInspections()
            if (inspections.isEmpty) Seq.empty
            else
              Seq(s"-P:scapegoat:disabledInspections:${inspections.mkString(":")}")
          }

          val enabledInspections = {
            val inspections = scapegoatEnabledInspections()
            if (inspections.isEmpty) Seq.empty
            else
              Seq(s"-P:scapegoat:enabledInspections:${inspections.mkString(":")}")
          }

          val ignoredFilePatterns = {
            val patterns = scapegoatIgnoredFiles()
            if (patterns.isEmpty) Seq.empty
            else
              Seq(s"-P:scapegoat:ignoredFiles:${patterns.mkString(":")}")
          }

          val reportFormats = {
            val formats = scapegoatReportFormats()
            if (formats.isEmpty) Seq.empty
            else
              Seq(s"-P:scapegoat:reports:${formats.mkString(":")}")
          }

          baseOptions ++ disabledInspections ++ enabledInspections ++ ignoredFilePatterns ++ reportFormats
      }
    }
  }

  /** Source directory prefix for reports. */
  def scapegoatSourcePrefix: T[String] = Task {
    sources() match {
      case Seq(source) => source.path.subRelativeTo(moduleDir).toString
      case other       =>
        val dirs = other.map(_.path.subRelativeTo(moduleDir).toString).mkString(", ")
        Task.fail(s"Scapegoat expected exactly one source directory, got ${other.size}: $dirs")
    }
  }

  /** Whether to enable verbose output. */
  def scapegoatVerbose: T[Boolean] = Task { true }

  /** Scapegoat version to use. */
  def scapegoatVersion: T[String]

  override def scalacOptions: T[Seq[String]] = super.scalacOptions() ++ scapegoatScalacOptions()

  private def scapegoatPluginClasspath: T[Seq[PathRef]] = Task {
    defaultResolver().classpath(
      Seq(mvn"$PluginGroupId:${PluginArtifactId}_${scalaVersion()}:${scapegoatVersion()}")
        .map(Lib.depToBoundDep(_, scalaVersion())),
      resolutionParamsMapOpt = Some(_.withScalaVersion(scalaVersion())),
    )
  }
}

object ScapegoatModule {

  private[scapegoat] val PluginGroupId    = "com.sksamuel.scapegoat"
  private[scapegoat] val PluginArtifactId = "scalac-scapegoat-plugin"
}
