package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Wall extends Item {

  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation()
  isBlockingMovement = true

  override def getItemType = Sprites.wall
}