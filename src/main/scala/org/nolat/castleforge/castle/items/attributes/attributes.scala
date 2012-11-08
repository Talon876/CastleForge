package org.nolat.castleforge.castle.items.attributes

import org.newdawn.slick.Color
import javax.swing.JFrame
import org.nolat.castleforge.tools.Preamble._

trait Attribute {}

object IDColor {
  val WHITE = Color.white
  val RED = Color.red
  val ORANGE = new Color(255, 158, 4)
  val YELLOW = Color.yellow
  val GREEN = Color.green
  val BLUE = Color.cyan
  val PURPLE = new Color(208, 27, 224)

  def fromString(color: String) = {
    color.toLowerCase match {
      case "white" => WHITE
      case "red" => RED
      case "orange" => ORANGE
      case "yellow" => YELLOW
      case "green" => GREEN
      case "blue" => BLUE
      case "purple" => PURPLE
      case _ => WHITE
    }
  }

  def toString(color: Color) = {
    color match {
      case WHITE => "white"
      case RED => "red"
      case ORANGE => "orange"
      case YELLOW => "yellow"
      case GREEN => "green"
      case BLUE => "blue"
      case PURPLE => "purple"
    }
  }
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

  def fromString(quantity: String) = {
    quantity.toLowerCase match {
      case "0" => ZERO
      case "1" => ONE
      case "3" => THREE
      case "5" => FIVE
      case "10" => TEN
      case "15" => FIFTEEN
      case "20" => TWENTY
      case _ => quantity.intOption.getOrElse(ZERO)
    }
  }

  def fromInt(quantity: Int) = {
    quantity match {
      case 0 => ZERO
      case 1 => ONE
      case 3 => THREE
      case 5 => FIVE
      case 10 => TEN
      case 15 => FIFTEEN
      case 20 => TWENTY
      case _ => ONE
    }

  }
}
trait Quantity extends Attribute {
  var quantity = Quantity.ZERO
}

object Shape {
  val DIAMOND = "diamond"
  val PENTAGON = "pentagon"
  val TRIANGLE = "triangle"
  val STAR = "star"

  def fromString(shape: String) = {
    shape.toLowerCase match {
      case "diamond" => DIAMOND
      case "pentagon" => PENTAGON
      case "triangle" => TRIANGLE
      case "star" => STAR
      case _ => DIAMOND
    }
  }
}
trait Shape extends Attribute {
  var shape = Shape.DIAMOND
}

object Luminosity {
  val LOW = 2
  val MEDIUM = 3
  val HIGH = 5

  def fromString(lum: String) = {
    lum.toLowerCase match {
      case "low" => LOW
      case "medium" => MEDIUM
      case "high" => HIGH
      case _ => LOW
    }
  }
  
  def toString(lumen: Luminosity) : String ={
    lumen.luminosity match {
      case LOW => "low"
      case MEDIUM => "medium"
      case HIGH => "high"
      case _ => "low"
    }
  }
  def toString(lumen: Int) : String ={
    lumen match {
      case LOW => "low"
      case MEDIUM => "medium"
      case HIGH => "high"
      case _ => "low"
    }
  }
}
trait Luminosity extends Attribute {
  var luminosity = Luminosity.LOW
}

object Direction {
  val WEST = 270
  val SOUTH = 180
  val EAST = 90
  val NORTH = 0

  def fromString(dir: String) = {
    dir.toLowerCase match {
      case "north" | "0" => NORTH
      case "west" | "270" => WEST
      case "south" | "180" => SOUTH
      case "east" | "90" => EAST
      case _ => NORTH
    }
  }

}
trait Direction extends Attribute {
  var direction = Direction.NORTH
}

object TorchState {
  val ON = true
  val OFF = false
  def fromBoolean(bool: Boolean) = {
    if (bool) ON else OFF
  }
}
trait TorchState {
  var torchstate = TorchState.ON
}

object CheckPointState {
  val ACTIVE = true
  val INACTIVE = false

  def fromBoolean(bool: Boolean) = {
    if (bool) ACTIVE else INACTIVE
  }
}
trait CheckPointState extends Attribute {
  var checkpointstate = CheckPointState.INACTIVE

  def activate() {
    checkpointstate = CheckPointState.ACTIVE
  }

  def deactivate() {
    checkpointstate = CheckPointState.INACTIVE
  }
}

