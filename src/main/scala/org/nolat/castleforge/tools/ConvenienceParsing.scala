package org.nolat.castleforge.tools

/**
 * This file holds classes/implicit methods for using the option pattern:
 * http://www.codecommit.com/blog/scala/the-option-pattern
 *
 * To use: import the Preamble object to get all of the implicit methods
 * import com.sri.penrad.scalautils.Preamble._
 * Ex:
 * val someString = "42"
 * val someInt = someString.intOption.getOrElse(0) //someInt will be 42
 * val anotherString = "asdf"
 * val anotherInt = someString.intOption.getOrElse(0) //anotherInt will be 0
 * Check ConvenienceParsing_Test for more examples
 */
class ConvenienceString(s: String) {
  private def convert[T](t: => T) = {
    try { Some(t) } catch {
      case nfe: NumberFormatException => None
    }
  }

  def booleanOption = if (s == null) { None } else {
    s.toLowerCase match {
      case "yes" | "true" | "1" => Some(true)
      case "no" | "false" | "0" => Some(false)
      case _ => None
    }
  }
  def byteOption = convert(s.toByte)
  def doubleOption = convert(s.toDouble)
  def floatOption = convert(s.toFloat)
  def hexOption = convert(java.lang.Integer.valueOf(s, 16))
  def hexLongOption = convert(java.lang.Long.valueOf(s, 16))
  def intOption = convert(s.toInt)
  def longOption = convert(s.toLong)
  def shortOption = convert(s.toShort)
}

object Preamble {
  implicit def string2convenienceString(s: String): ConvenienceString = new ConvenienceString(s)
}