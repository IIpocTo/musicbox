package musicbox.utils

import java.math.BigInteger
import java.util.concurrent.ThreadLocalRandom

object SessionUtil {

  private val HexArray = "0123456789ABCDEF".toCharArray

  private[utils] def toHexString(bytes: Array[Byte]): String = {
    val hexChars = new Array[Char](bytes.length * 2)
    var j = 0
    while (j < bytes.length) {
      val v = bytes(j) & 0xFF
      hexChars(j * 2) = HexArray(v >>> 4)
      hexChars(j * 2 + 1) = HexArray(v & 0x0F)
      j += 1
    }
    new String(hexChars)
  }

  private[utils] def hexStringToByte(hexString: String): Array[Byte] = {
    val len = hexString.length
    val data = new Array[Byte](len / 2)
    var i = 0
    while (i < len) {
      data(i / 2) = ((Character.digit(hexString.charAt(i), 16) << 4) +
        Character.digit(hexString.charAt(i + 1), 16)).toByte
      i += 2
    }
    data
  }

  def constantTimeEquals(a: String, b: String): Boolean = {
    if (a.length != b.length) false
    else {
      var equal = 0
      for (i <- Array.range(0, a.length)) {
        equal |= a(i) ^ b(i)
      }
      equal == 0
    }
  }

  def randomString(length: Int): String = {
    val random: ThreadLocalRandom = ThreadLocalRandom.current()
    new BigInteger(length * 5, random).toString(32) // because 2^5 = 32
  }

  def randomServerSecret(): String = randomString(128)

}
