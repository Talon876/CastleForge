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
import org.nolat.castleforge.tools.Lerper

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

  var movementLerper = Lerper(sprite.animationLength)
  movementLerper.finishedLerp = stopWalking

  val speed = .2f

  def stopWalking() = {
    sprite.setAnimation("idle")
    state = PlayerState.IDLE
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (state == PlayerState.IDLE) {
      if (container.getInput().isKeyDown(Input.KEY_W)) {

        sprite.setAnimation("walking_up")
        state = PlayerState.WALKING_UP
        movementLerper.start(position.y, position.y - 64)
        movementLerper.msToLerp = sprite.animationLength
      } else if (container.getInput().isKeyDown(Input.KEY_S)) {

        sprite.setAnimation("walking_down")
        state = PlayerState.WALKING_DOWN
        movementLerper.start(position.y, position.y + 64)
        movementLerper.msToLerp = sprite.animationLength
      } else if (container.getInput().isKeyDown(Input.KEY_A)) {

        sprite.setAnimation("walking_left")
        state = PlayerState.WALKING_LEFT
        movementLerper.start(position.x, position.x - 64)
        movementLerper.msToLerp = sprite.animationLength
      } else if (container.getInput().isKeyDown(Input.KEY_D)) {

        sprite.setAnimation("walking_right")
        state = PlayerState.WALKING_RIGHT
        movementLerper.start(position.x, position.x + 64)
        movementLerper.msToLerp = sprite.animationLength
      }
    } else { //not idle, moving
      if (state == PlayerState.WALKING_LEFT || state == PlayerState.WALKING_RIGHT) {
        position = new Vector2f(movementLerper.value, position.y)
      } else if (state == PlayerState.WALKING_UP || state == PlayerState.WALKING_DOWN) {
        position = new Vector2f(position.x, movementLerper.value)
      }

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(position.x.toInt, position.y.toInt, Color.white)
  }
}