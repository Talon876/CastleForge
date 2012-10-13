package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.CheckPointState
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class CheckPoint extends Item with CheckPointState {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.checkpoint
}