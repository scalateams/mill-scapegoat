package foo

object Foo {

  def main(args: Array[String]): Unit = {
    val args = "ignored variable shadowing" // ignored `VariableShadowing`
    val res  = 0 / args.size                // ignored `ZeroNumerator`
    var who  = "world"                      // triggers `VarCouldBeVal`
    println("Hello, " + who + "! My result is " + res)
  }
}
