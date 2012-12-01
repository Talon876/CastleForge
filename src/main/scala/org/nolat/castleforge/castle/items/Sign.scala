package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Readable

class Sign(signText: String) extends Item with Readable {
  displayText = signText

  def this(paramList: List[String]) = {
    this(paramList(0))
  }
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.sign

  override def getParamList = {
    Seq(displayText)
  }

  override def getOptions = {
    List("text")
  }

  override def setOptions(options: List[Any]) {
    displayText = options(0).asInstanceOf[String]
  }
}