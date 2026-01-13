package foo

object Bar {

  def ignored(input: Int): Int = {
    Option(input).get // should trigger `OptionGet` but file is ignored
  }
}
