package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprites.value2string
import org.nolat.castleforge.castle.items.attributes.RNG

class Ice extends Item with RNG {
  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation()

  override def getItemType = Sprites.ice
}