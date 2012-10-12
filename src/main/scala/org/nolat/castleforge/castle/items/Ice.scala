package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Ice extends Item with RNG {

  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation()

  override def getItemType = Sprites.ice
}