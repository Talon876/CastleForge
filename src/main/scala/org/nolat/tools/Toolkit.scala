package org.nolat.tools

import java.util.Random
import scala.math
import java.awt.Color

object Toolkit {
  val random = new Random()
  val PI = math.Pi.asInstanceOf[Float]
  val PiOver2 = (PI / 2.0f).asInstanceOf[Float]
  val PiOver4 = (PI / 4.0f).asInstanceOf[Float]
  val TwoPi = (2.0 * PI).asInstanceOf[Float]

  def randomRange(min: Float, max: Float): Float = (random.nextDouble() * (max - min) + min).asInstanceOf[Float]

  def randomRange(min: Int, max: Int): Int = random.nextInt(max - min + 1) + min

  def randomColor(alpha: Boolean): Color = {
    val r = randomRange(0, 255)
    val g = randomRange(0, 255)
    val b = randomRange(0, 255)
    val a = randomRange(0, 255)
    if (alpha) new Color(r, g, b, a) else new Color(r, g, b)
  }

  def randomColor(): Color = randomColor(false)

  def randomAngle() = randomRange(0, TwoPi)

  def lerp(value1: Float, value2: Float, amount: Float) = {
    if (value1 == value2) {
      value2
    } else {
      val result = value1 + (value2 - value1) * amount
      result
    }
  }
}
