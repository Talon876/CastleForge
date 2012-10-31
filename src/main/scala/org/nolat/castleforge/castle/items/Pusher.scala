package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.Direction
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Player
import org.newdawn.slick.Input

class Pusher(_direction: String) extends Item with Direction {
  direction = Direction.fromString(_direction)

  def this(paramList: List[String]) = {
    this(paramList(0))
  }
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.pusher
  val speedMod = 2.2f

  override def getParamList = {
    Seq(direction.toString)
  }

  override def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.pushTransform()
    g.rotate(x + Config.TileWidth / 2, y + Config.TileHeight / 2, direction)
    sprite.getAnimation.draw(x, y, color)
    g.popTransform()
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    direction match {
      case Direction.NORTH => player.attemptMove(Input.KEY_W, "gliding_up", speedMod)
      case Direction.SOUTH => player.attemptMove(Input.KEY_S, "gliding_down", speedMod)
      case Direction.EAST => player.attemptMove(Input.KEY_D, "gliding_right", speedMod)
      case Direction.WEST => player.attemptMove(Input.KEY_A, "gliding_left", speedMod)
    }
  }
}