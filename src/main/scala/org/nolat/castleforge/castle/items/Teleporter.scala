package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprites.value2string

class Teleporter extends Item with IDColor {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.teleporter
}