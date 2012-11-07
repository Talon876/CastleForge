package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Collectable

class Match(_quantity: Int) extends Item with Quantity with Collectable {
  quantity = Quantity.fromInt(_quantity)

  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(1))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation(getAnimationFromQuantity)

  override def getItemType = Sprites.matchh

  override def getParamList = {
    Seq(quantity.toString)
  }

  private def getAnimationFromQuantity = {
    if (quantity >= 1 && quantity < 3) {
      "one"
    } else if (quantity >= 3 && quantity < 6) {
      "few"
    } else if (quantity >= 6 && quantity < 10) {
      "many"
    } else {
      "alot"
    }
  }

  override def isSimilar(that: Collectable): Boolean = {
    that match {
      case matchh: Match => true
      case _ => false
    }
  }
}