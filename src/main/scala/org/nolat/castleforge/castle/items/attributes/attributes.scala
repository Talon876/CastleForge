package org.nolat.castleforge.castle.items.attributes

import org.newdawn.slick.Color
import javax.swing.JFrame

trait Attribute {}

object IDColor {
  val WHITE = Color.white
  val BLACK = Color.black
  val RED = Color.red
  val ORANGE = Color.orange
  val YELLOW = Color.yellow
  val GREEN = Color.green
  val BLUE = Color.blue
  val PURPLE = new Color(208, 27, 224)
}
trait IDColor extends Attribute {
  var idcolor = IDColor.WHITE
}

object Quantity {
  val ZERO = 0
  val ONE = 1
  val THREE = 3
  val FIVE = 5
  val TEN = 10
  val FIFTEEN = 15
  val TWENTY = 20
}
trait Quantity extends Attribute {
  var quantity = Quantity.ZERO
}

object Shape {
  val DIAMOND = "diamond"
  val PENTAGON = "pentagon"
  val TRIANGLE = "triangle"
  val STAR = "star"
}
trait Shape extends Attribute {
  var shape = Shape.DIAMOND
}

object Luminosity {
  val OFF = 0
  val LOW = 3
  val MEDIUM = 7
  val HIGH = 15
}
trait Luminosity extends Attribute {
  var luminosity = Luminosity.OFF
}

object Direction {
  val NORTH = scala.math.toRadians(90).toFloat
  val WEST = scala.math.toRadians(180).toFloat
  val SOUTH = scala.math.toRadians(270).toFloat
  val EAST = scala.math.toRadians(0).toFloat
}
trait Direction extends Attribute {
  var direction = Direction.NORTH
}

object TorchState {
  val ON = true
  val OFF = false
}
trait TorchState {
  var torchstate = TorchState.ON
}

object CheckPointState {
  val ACTIVE = true
  val INACTIVE = false
}
trait CheckPointState extends Attribute {
  var checkpointstate = CheckPointState.INACTIVE
}