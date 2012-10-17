package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.castle.items.attributes.Shape
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Key(_color: String, _shape: String, _quantity: Int) extends Item with IDColor with Quantity with Shape {

  idcolor = IDColor.fromString(_color)
  shape = Shape.fromString(_shape)
  quantity = Quantity.fromInt(_quantity)

  def this(paramList: List[String]) = {
    this(paramList(0), paramList(1), paramList(2).intOption.getOrElse(1))
  }

  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.key

  override def getParamList = {
    Seq(idcolor.toString, shape, quantity.toString)
  }
}