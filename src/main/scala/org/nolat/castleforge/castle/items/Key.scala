package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.castle.items.attributes.Shape
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Key extends Item with IDColor with Quantity with Shape {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.key
}