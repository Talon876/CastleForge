package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.IDColor

class Torch extends Item with IDColor {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.torch
}