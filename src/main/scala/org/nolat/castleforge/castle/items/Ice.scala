package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Ice extends Item with RNG {

  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation(List(.33f, .33f, .33f, .01f))

  override def getItemType = Sprites.floor
}