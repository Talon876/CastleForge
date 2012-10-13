package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Door extends Item with IDColor with Quantity {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.door
}