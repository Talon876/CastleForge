package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Match(_quantity: Int) extends Item with Quantity {
  quantity = Quantity.fromInt(_quantity)

  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(1))
  }

  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.matchh

  override def getParamList = {
    Seq(quantity.toString)
  }
}