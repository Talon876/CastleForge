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

class Ice extends Item with RNG {
  sprite = new Sprite(getItemType)
  sprite.setRandomAnimation()

  override def getItemType = Sprites.ice

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    val reverseMap = player.movementMap.map(_.swap)

    player.attemptMove(reverseMap(player.lastMoveDirection))
  }
}