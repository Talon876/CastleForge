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
  val speedMod = 2.2f

  override def getItemType = Sprites.ice

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    val reverseMap = player.movementMap.map(_.swap)

    reverseMap(player.lastMoveDirection) match {
      case Input.KEY_W => player.attemptMove(Input.KEY_W, "gliding_up", speedMod)
      case Input.KEY_S => player.attemptMove(Input.KEY_S, "gliding_down", speedMod)
      case Input.KEY_A => player.attemptMove(Input.KEY_A, "gliding_left", speedMod)
      case Input.KEY_D => player.attemptMove(Input.KEY_D, "gliding_right", speedMod)
    }
  }
}