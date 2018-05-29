package musicbox.util

import musicbox.utils.SessionUtil
import org.scalatest.{FlatSpec, Matchers}

class SessionUtilTest extends FlatSpec with Matchers {

  "constantTimeEquals" should "return true for equal elems" in {
    val a = "xxxxccccvvv"
    val b = "xxxxccccvvv"
    SessionUtil.constantTimeEquals(a, b) shouldBe true
  }

  "constantTimeEquals" should "return false for non equal elems" in {
    val a = "xxxxccccvvv"
    val b = "xxxxccccvvvss"
    SessionUtil.constantTimeEquals(a, b) shouldBe false
  }

}
