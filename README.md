# mill-scapegoat

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

```shell script
> mill project.compile
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

```sh
./mill __.reformat + __.test + __.publishLocal
```

All contributions are welcome!
