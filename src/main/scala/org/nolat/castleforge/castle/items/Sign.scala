package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Sign extends Item {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.sign
}