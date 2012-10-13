package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Match extends Item with Quantity {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.matchh
}