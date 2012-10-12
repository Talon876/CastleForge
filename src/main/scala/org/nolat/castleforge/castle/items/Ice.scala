package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Ice extends Item with RNG {

  sprite = new Sprite(getItemType)
  println("made the sprite")
  sprite.setRandomAnimation(List(.33f, .33f, .33f, .01f))

  override def getItemType = Sprites.ice
}