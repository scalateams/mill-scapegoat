# mill-scapegoat

![CI passing badge](https://github.com/scalateams/mill-scapegoat/actions/workflows/ci.yml/badge.svg?branch=main) ![Maven Central version badge](https://img.shields.io/maven-central/v/org.scalateams/mill-scapegoat_mill1_3) [![Scaladoc badge](https://img.shields.io/badge/Scaladoc-gray)](https://javadoc.io/doc/org.scalateams/mill-scapegoat_mill1_3/latest/index.html) [![Scala Steward helping badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://github.com/scala-steward-org/scala-steward) [![GitHub stars badge](https://img.shields.io/github/stars/scalateams/mill-scapegoat?style=social)](https://github.com/scalateams/mill-scapegoat)

A [scapegoat](https://github.com/sksamuel/scapegoat) plugin for Mill build tool.

## Usage

*build.mill*:
```scala
//| mvnDeps:
//| - org.scalateams::mill-scapegoat::0.1.0
import org.scalateams.mill.scapegoat.ScapegoatModule
import mill.scalalib.*

object project extends ScalaModule with ScapegoatModule {

  def scalaVersion     = "3.3.7"
  def scapegoatVersion = "3.2.4"
}
```

```console
$ ./mill project.compile
Compiling compiler interface...
...
[warn] /project/project/src/MyClass.scala:6:9: local var who in method main is never updated: consider using immutable val
[warn]     var who = "world"
[warn]         ^
[info] [scapegoat] 123 activated inspections
[warn] [scapegoat] Analysis complete: 1 files - 0 errors 1 warns 0 infos
[info] [scapegoat] Written HTML report [out/scapegoatOutput.dest/scapegoat.html]
...
done compiling
```

### Overriding Scala compiler options:

Make sure you include `super.scalacOptions()` or `scapegoatScalacOptions()` directly when overriding `scalacOptions` so that plugins options are included.

```scala
override def scalacOptions = super.scalacOptions() ++ Seq("some", "options")
```

## Related projects

* [scapegoat](https://github.com/sksamuel/scapegoat)
* Inspired by [sbt-scapegoat](https://github.com/scapegoat-scala/sbt-scapegoat)


## Contributing

Before committing run:

```console
$ ./mill __.style + __.test + __.publishLocal
```

All contributions are welcome!
