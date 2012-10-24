package org.nolat.castleforge.castle

import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.Config
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.castle.items.GameItem
import org.nolat.castleforge.graphics.Sprite
import org.newdawn.slick.Color

class Player extends GameItem {

  object PlayerState extends Enumeration {
    type PlayerState = Value
    val WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT, IDLE = Value
  }

  val inventory: Inventory = new Inventory

  position = new Vector2f(8, 8)
  sprite = new Sprite(Sprites.player)
  sprite.setAnimation("idle")

  var state = PlayerState.IDLE

  val speed = .2f

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (state == PlayerState.IDLE) {
      if (container.getInput().isKeyDown(Input.KEY_W)) {
        position.y -= 1 * speed
        sprite.setAnimation("walking_up")
      } else if (container.getInput().isKeyDown(Input.KEY_S)) {
        position.y += 1 * speed
        sprite.setAnimation("walking_down")
      } else if (container.getInput().isKeyDown(Input.KEY_A)) {
        position.x -= 1 * speed
        sprite.setAnimation("walking_left")
      } else if (container.getInput().isKeyDown(Input.KEY_D)) {
        position.x += 1 * speed
        sprite.setAnimation("walking_right")
      }
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(position.x.toInt, position.y.toInt, Color.white)
  }
}