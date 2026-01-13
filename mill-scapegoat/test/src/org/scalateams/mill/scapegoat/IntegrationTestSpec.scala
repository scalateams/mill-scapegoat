package org.scalateams.mill.scapegoat

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import mill.testkit.IntegrationTester
import os.RelPath

class IntegrationTestSpec extends AnyWordSpec with Matchers {

  def test(project: RelPath): IntegrationTester = {
    val resourceFolder = os.Path(sys.env("MILL_TEST_RESOURCE_DIR"))
    new IntegrationTester(
      daemonMode = true,
      workspaceSourcePath = resourceFolder / project,
      millExecutable = os.Path(sys.env("MILL_EXECUTABLE_PATH")),
    )
  }

  "integrations" should {

    "report" in {
      val tester = test(RelPath("integration/report"))
      val result = tester.eval(Seq("__.compile"))
      assert(result.isSuccess, result.err)

      val outputDir = tester.workspacePath / "out" / "scapegoatOutput.dest"

      os.exists(outputDir) shouldBe true

      os.exists(outputDir / "scapegoat-gitlab.json") shouldBe true
      os.exists(outputDir / "scapegoat-scalastyle.xml") shouldBe true
      os.exists(outputDir / "scapegoat.html") shouldBe true
      os.exists(outputDir / "scapegoat.md") shouldBe true
      os.exists(outputDir / "scapegoat.xml") shouldBe true
    }
  }
}
