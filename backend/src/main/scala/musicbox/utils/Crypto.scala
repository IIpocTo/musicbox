package musicbox.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util
import java.util.Base64

import javax.crypto.{Cipher, Mac}
import javax.crypto.spec.SecretKeySpec
import musicbox.utils.SessionUtil._

object Crypto {

  def signHmacSHA1hex(message: String, secret: String): String = {
    val key = secret.getBytes(StandardCharsets.UTF_8)
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(new SecretKeySpec(key, "HmacSHA1"))
    toHexString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)))
  }

  def signHmacSHA256base64(message: String, secret: String): String = {
    val key = secret.getBytes(StandardCharsets.UTF_8)
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(key, "HmacSHA256"))
    Base64.getUrlEncoder
      .withoutPadding()
      .encodeToString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)))
  }

  def hashSHA256(value: String): String = {
    val digest = MessageDigest.getInstance("SHA-256")
    toHexString(digest.digest(value.getBytes(StandardCharsets.UTF_8)))
  }

  def encryptAES(value: String, secret: String): String = {
    val raw = util.Arrays.copyOf(secret.getBytes(StandardCharsets.UTF_8), 16)
    val secretKeySpec = new SecretKeySpec(raw, "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    toHexString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)))
  }

  def decrypt_AES(value: String, secret: String): String = {
    val raw = util.Arrays.copyOf(secret.getBytes("utf-8"), 16)
    val secretKeySpec = new SecretKeySpec(raw, "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    new String(cipher.doFinal(hexStringToByte(value)))
  }

}
