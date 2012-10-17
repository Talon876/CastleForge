package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Shape

class Door(var doorType: Int, _idColor: String, _shape: String) extends Item with IDColor with Shape {
  //doorType: 0 = normal, 1 = locked, 2 = hidden
  idcolor = IDColor.fromString(_idColor)
  shape = Shape.fromString(_shape)

  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(0), paramList(1), paramList(2))
  }

  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.door

  override def getParamList = {
    Seq(doorType.toString, idcolor.toString, shape)
  }
}