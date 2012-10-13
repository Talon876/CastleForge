package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Obstacle extends Item {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.obstacle
}