package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.Direction
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Pusher(_direction: String) extends Item with Direction {
  direction = Direction.fromString(_direction)

  def this(paramList: List[String]) = {
    this(paramList(0))
  }
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.pusher

  override def getParamList = {
    Seq(direction.toString)
  }
}