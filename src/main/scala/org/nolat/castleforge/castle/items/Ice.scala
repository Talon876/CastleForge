package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprites.value2string
import org.nolat.castleforge.castle.items.attributes.RNG
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Player
import org.newdawn.slick.Color
import org.newdawn.slick.Input
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.tools.MoveDescription

class Ice extends Item with RNG {
  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation()
  val speedMod = 2.2f

  override def getItemType = Sprites.ice

  override def onPlayerEnter(player: Player, srcFloor: Floor) {

    player.attemptMove(MoveDescription(player.lastMove.keyPressed, player.lastMove.animation.replace("walking_", "gliding_"), speedMod))
  }
}