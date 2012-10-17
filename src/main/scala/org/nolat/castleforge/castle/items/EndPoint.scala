package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Readable

class EndPoint(endText: String) extends Item with Readable {
  displayText = endText

  def this(paramList: List[String]) = {
    this(paramList(0))
  }

  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.endpoint

  override def getParamList = {
    Seq(displayText)
  }
}