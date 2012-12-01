package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Readable
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor

class EndPoint(endText: String) extends Item with Readable {
  displayText = endText

  def this(paramList: List[String]) = {
    this(paramList(0))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation("default")

  override def getItemType = Sprites.endpoint

  override def getParamList = {
    Seq(displayText)
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    println(displayText)
  }

  override def getOptions = {
    List("text")
  }

  override def setOptions(options: List[Any]) {
    displayText = options(0).asInstanceOf[String]
  }
}