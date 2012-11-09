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
import scala.collection.mutable.Queue
import org.nolat.castleforge.tools.MoveDescription
import org.nolat.castleforge.castle.items._
import org.nolat.castleforge.castle.items.attributes.Collectable

object PlayerState extends Enumeration {
  type PlayerState = Value
  val WALKING_UP, WALKING_DOWN, WALKING_RIGHT, WALKING_LEFT, IDLE = Value
}

class Player(var castle: Castle) extends GameItem {

  val inventory: Inventory = new Inventory

  var tilePosition = CastleUtil.findPlayerStart(castle).getTilePosition

  position = new Vector2f(Config.TileOffsetX + Config.TileWidth * 5, Config.TileOffsetY + Config.TileHeight * 5)

  var tileOffset = (tilePosition._1 - 5, tilePosition._2 - 5)
  var movementOffset = (tileOffset._1 * 64f, tileOffset._2 * 64f)

  sprite = new Sprite(Sprites.player)
  sprite.setAnimation("idle")

  var state = PlayerState.IDLE

  var movementLerper = Lerper(sprite.animationLength)
  movementLerper.finishedLerp = stopWalking

  val speed = .2f

  private var lastTile: Floor = null
  var lastMove: MoveDescription = null

  private var moveQueue: Queue[MoveDescription] = new Queue()

  val movementMap = Map(
    Input.KEY_W -> (0, -1),
    Input.KEY_S -> (0, 1),
    Input.KEY_A -> (-1, 0),
    Input.KEY_D -> (1, 0))

  private val reverseDirectionMap = Map(
    Input.KEY_W -> Input.KEY_S,
    Input.KEY_S -> Input.KEY_W,
    Input.KEY_A -> Input.KEY_D,
    Input.KEY_D -> Input.KEY_A)

  private val keyToAnimationMap = Map(
    Input.KEY_W -> "walking_up",
    Input.KEY_S -> "walking_down",
    Input.KEY_A -> "walking_left",
    Input.KEY_D -> "walking_right")

  private val stateMap = Map(
    PlayerState.WALKING_UP -> (0, -1),
    PlayerState.WALKING_DOWN -> (0, 1),
    PlayerState.WALKING_LEFT -> (-1, 0),
    PlayerState.WALKING_RIGHT -> (1, 0))

  private def stateMapReverse = stateMap.map(_.swap)

  def container = {
    castle.map(tilePosition._2)(tilePosition._1)
  }

  def reverseMove(md: MoveDescription) = {
    val reverseKey = reverseDirectionMap(md.keyPressed)
    MoveDescription(reverseKey, keyToAnimationMap(reverseKey), md.speedModifier, md.ghost)
  }

  def stopWalking() = {
    tilePosition = (tilePosition._1 + stateMap(state)._1, tilePosition._2 + stateMap(state)._2)
    tileOffset = (tileOffset._1 + stateMap(state)._1, tileOffset._2 + stateMap(state)._2)
    movementOffset = (tileOffset._1 * 64, tileOffset._2 * 64)
    //    println("Position: " + tilePosition)
    //    println("Offset: " + tileOffset)

    sprite.setAnimation("idle")
    state = PlayerState.IDLE
    val destItem = castle.map(tilePosition._2)(tilePosition._1)
    if (!lastMove.ghost) destItem.onPlayerEnter(this, lastTile) //only send updates when not ghosting

    playMovement()
  }

  def teleportMove(destinationTile: (Int, Int), animation: String, speedModifier: Float) {
    val horizontal = destinationTile._1 - tilePosition._1
    val vertical = destinationTile._2 - tilePosition._2
    val horizKey = if (horizontal < 0) Input.KEY_A else Input.KEY_D
    val vertKey = if (vertical < 0) Input.KEY_W else Input.KEY_S

    for (x <- 0 to scala.math.abs(horizontal) - 1) {
      enqueueMove(MoveDescription(horizKey, animation, speedModifier, true))
    }
    for (y <- 0 to scala.math.abs(vertical) - 1) {
      enqueueMove(MoveDescription(vertKey, animation, speedModifier, true))
    }

  }

  def enqueueMove(moveDescription: MoveDescription) {
    moveQueue.enqueue(moveDescription)
  }

  def playMovement() {
    if (!moveQueue.isEmpty) {
      attemptMove(moveQueue.dequeue())
    }
  }

  /**
   * attemptMove method that passes in the correct walking animation for the direction
   * @param keyPressed the key that was pressed
   */
  def attemptMove(keyPressed: Int) {
    keyPressed match {
      case Input.KEY_W => attemptMove(Input.KEY_W, "walking_up", 1f)
      case Input.KEY_S => attemptMove(Input.KEY_S, "walking_down", 1f)
      case Input.KEY_A => attemptMove(Input.KEY_A, "walking_left", 1f)
      case Input.KEY_D => attemptMove(Input.KEY_D, "walking_right", 1f)
    }
  }

  def attemptMove(keyPressed: Int, animation: String, speedModifier: Float) {
    attemptMove(MoveDescription(keyPressed, animation, speedModifier))
  }

  def attemptMove(md: MoveDescription) {
    val sourceFloor = castle.getFloorAtPosition(tilePosition)
    lastTile = sourceFloor
    val destFloor = castle.getFloorAtPositionWithOffset(tilePosition, movementMap(md.keyPressed))

    //println("Destination Floor: " + destFloor.itemName + ": blocking? " + destFloor.isBlockingMovement)
    if (!destFloor.isBlockingMovement || md.ghost) {
      lastMove = md
      if (!md.ghost) sourceFloor.onPlayerExit(this, destFloor) //only send events when not ghosting
      sprite.setAnimation(md.animation)
      md.keyPressed match {
        case Input.KEY_W => {
          state = PlayerState.WALKING_UP
          movementLerper.start(position.y, position.y - Config.TileHeight)
        }
        case Input.KEY_S => {
          state = PlayerState.WALKING_DOWN
          movementLerper.start(position.y, position.y + Config.TileHeight)
        }
        case Input.KEY_A => {
          state = PlayerState.WALKING_LEFT
          movementLerper.start(position.x, position.x - Config.TileWidth)
        }
        case Input.KEY_D => {
          state = PlayerState.WALKING_RIGHT
          movementLerper.start(position.x, position.x + Config.TileWidth)
        }
      }
      movementLerper.msToLerp = (sprite.animationLength.toFloat / md.speedModifier).toInt //adjust lerp length to animation length and apply speed modifier
    } else {
      //println("You shall not pass!")
    }
  }

  def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (state == PlayerState.IDLE) {
      handleInput(container)
    } else { //not idle, moving
      //println(movementOffset)
      movementOffset = ((movementLerper.amountLerped * 64) * stateMap(state)._1 + tileOffset._1 * 64,
        (movementLerper.amountLerped * 64) * stateMap(state)._2 + tileOffset._2 * 64)
    }
  }

  private def handleInput(container: GameContainer) {
    if (container.getInput().isKeyDown(Input.KEY_W)) {
      attemptMove(Input.KEY_W)
    } else if (container.getInput().isKeyDown(Input.KEY_S)) {
      attemptMove(Input.KEY_S)
    } else if (container.getInput().isKeyDown(Input.KEY_A)) {
      attemptMove(Input.KEY_A)
    } else if (container.getInput().isKeyDown(Input.KEY_D)) {
      attemptMove(Input.KEY_D)
    } else if (container.getInput().isKeyDown(Input.KEY_U)) {
      enqueueMove(MoveDescription(Input.KEY_A, "walking_right", 1.25f))
      enqueueMove(MoveDescription(Input.KEY_A, "walking_right", 1.25f))
      enqueueMove(MoveDescription(Input.KEY_A, "walking_right", 1.25f))
      enqueueMove(MoveDescription(Input.KEY_A, "walking_right", 1.25f))
      playMovement()
    } else if (container.getInput().isKeyDown(Input.KEY_I)) {
      inventory.addItem(Item("key", List("red", "diamond", "1")).get.asInstanceOf[Collectable])
    } else if (container.getInput().isKeyDown(Input.KEY_E)) {
      inventory.clear()
    } else if (container.getInput().isKeyDown(Input.KEY_O)) {
      inventory.addItem(Item("crystal_ball").get.asInstanceOf[Collectable])
    }
  }

  def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(position.x.toInt, position.y.toInt, Color.white)
    //g.setColor(new Color(0, 0, 0, this.container.brightness))
    //g.fillRect(position.x.toInt, position.y.toInt, 64, 64)
  }
}