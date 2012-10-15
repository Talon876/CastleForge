package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class CrystalBall extends Item {
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.crystal_ball
}