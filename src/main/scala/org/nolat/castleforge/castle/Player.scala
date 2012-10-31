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
  var castle: Castle = null

  position = new Vector2f(8 + 64 * 5, 8 + 64 * 5)
  var tilePosition = (5, 5)
  sprite = new Sprite(Sprites.player)
  sprite.setAnimation("idle")

  var state = PlayerState.IDLE

  var movementLerper = Lerper(sprite.animationLength)
  movementLerper.finishedLerp = stopWalking

  val speed = .2f

  var lastTile: Floor = null
  var lastMoveDirection = (0, 0)

  val movementMap = Map(
    Input.KEY_W -> (0, -1),
    Input.KEY_S -> (0, 1),
    Input.KEY_A -> (-1, 0),
    Input.KEY_D -> (1, 0))

  private val stateMap = Map(
    PlayerState.WALKING_UP -> (0, -1),
    PlayerState.WALKING_DOWN -> (0, 1),
    PlayerState.WALKING_LEFT -> (-1, 0),
    PlayerState.WALKING_RIGHT -> (1, 0))

  var tileOffset = (0, 0)
  var movementOffset = (0f, 0f)

  def stopWalking() = {
    //correctPosition()
    tilePosition = (tilePosition._1 + stateMap(state)._1, tilePosition._2 + stateMap(state)._2)
    lastMoveDirection = stateMap(state)
    tileOffset = (tileOffset._1 + stateMap(state)._1, tileOffset._2 + stateMap(state)._2)
    movementOffset = (tileOffset._1 * 64, tileOffset._2 * 64)
    println("Position: " + tilePosition)
    println("Offset: " + tileOffset)

    sprite.setAnimation("idle")
    state = PlayerState.IDLE
    val destItem = castle.map(tilePosition._2)(tilePosition._1)
    destItem.onPlayerEnter(this, lastTile)
  }

  private def correctPosition() {
    if (state == PlayerState.WALKING_LEFT || state == PlayerState.WALKING_RIGHT) {
      position = new Vector2f(movementLerper.finish, position.y)
    } else if (state == PlayerState.WALKING_UP || state == PlayerState.WALKING_DOWN) {
      position = new Vector2f(position.x, movementLerper.finish)
    }
  }

  def attemptMove(keyPressed: Int) {
    val sourceItem = castle.map(tilePosition._2)(tilePosition._1)
    lastTile = sourceItem
    val destTile = (tilePosition._1 + movementMap(keyPressed)._1, tilePosition._2 + movementMap(keyPressed)._2)
    val destItem = castle.map(destTile._2)(destTile._1)

    //println("Destin: " + destItem.itemName + " at " + destTile)
    //     println("Position: " + position.x.toInt + " " + position.y.toInt)
    if (!destItem.isBlockingMovement) {
      sourceItem.onPlayerExit(this, destItem)

      println(tileOffset)
      keyPressed match {
        case Input.KEY_W => {
          sprite.setAnimation("walking_up")
          state = PlayerState.WALKING_UP
          movementLerper.start(position.y, position.y - Config.TileHeight)
        }
        case Input.KEY_S => {
          sprite.setAnimation("walking_down")
          state = PlayerState.WALKING_DOWN
          movementLerper.start(position.y, position.y + Config.TileHeight)
        }
        case Input.KEY_A => {
          sprite.setAnimation("walking_left")
          state = PlayerState.WALKING_LEFT
          movementLerper.start(position.x, position.x - Config.TileWidth)
        }
        case Input.KEY_D => {
          sprite.setAnimation("walking_right")
          state = PlayerState.WALKING_RIGHT
          movementLerper.start(position.x, position.x + Config.TileWidth)
        }
      }
      movementLerper.msToLerp = sprite.animationLength //adjust lerp length to animation length
    } else {
      println("You shall not pass!")
    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (state == PlayerState.IDLE) {
      if (container.getInput().isKeyDown(Input.KEY_W)) {
        attemptMove(Input.KEY_W)
      } else if (container.getInput().isKeyDown(Input.KEY_S)) {
        attemptMove(Input.KEY_S)
      } else if (container.getInput().isKeyDown(Input.KEY_A)) {
        attemptMove(Input.KEY_A)
      } else if (container.getInput().isKeyDown(Input.KEY_D)) {
        attemptMove(Input.KEY_D)
      } else if (container.getInput().isKeyDown(Input.KEY_U)) {
        attemptMove(Input.KEY_W)
      }
    } else { //not idle, moving
      //println(movementOffset)
      //(64 * movementLerper.amountLerped) * tileOffset._1) + (movementOffset._1 * stateMap(state)._1)
      movementOffset = ((movementLerper.amountLerped * 64) * stateMap(state)._1 + tileOffset._1 * 64,
        (movementLerper.amountLerped * 64) * stateMap(state)._2 + tileOffset._2 * 64)

      if (state == PlayerState.WALKING_LEFT || state == PlayerState.WALKING_RIGHT) {
        //position = new Vector2f(movementLerper.value, position.y)
      } else if (state == PlayerState.WALKING_UP || state == PlayerState.WALKING_DOWN) {
        //position = new Vector2f(position.x, movementLerper.value)
      }
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(position.x.toInt, position.y.toInt, Color.white)
  }
}