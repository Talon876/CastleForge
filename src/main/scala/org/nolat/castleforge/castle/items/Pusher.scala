package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.Direction
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Pusher extends Item with Direction {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.pusher
}