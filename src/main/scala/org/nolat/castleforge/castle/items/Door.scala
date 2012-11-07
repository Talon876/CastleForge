package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Shape
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.graphics.Sprites
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.tools.MoveDescription
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil

class Door(var doorType: Int, _idColor: String = "", _shape: String = "") extends Item with IDColor with Shape {
  //doorType: 0 = normal, 1 = locked, 2 = hidden
  idcolor = IDColor.fromString(_idColor)
  shape = Shape.fromString(_shape)

  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(0), if (paramList.length > 1) paramList(1) else "", if (paramList.length > 1) paramList(2) else "")
  }

  sprite = new Sprite(getItemType)
  val wallSprite = new Sprite(Sprites.wall)

  doorType match {
    case 0 => sprite.setAnimation("unlocked")
    case 1 => sprite.setAnimation("locked")
    case 2 => sprite.setAnimation("unlocked")
  }

  val keySprite = new Sprite(Sprites.key)
  keySprite.setAnimation(shape)

  private var opacity = .25f

  /**
   * Determines if the player can open this door
   * @param player The player who's inventory should be checked
   * @return true if the player posses a key to open this door if its locked, or a crystal ball if it's hidden
   */
  def canBeOpenedBy(player: Player) = {
    var canOpen = false
    val validKeys = getValidKeyFromPlayer(player)
    if (validKeys.size > 0) {
      canOpen = true
    }
    canOpen
  }

  /**
   * @param player The player who's inventory should be checked
   * @return the keys in the player's inventory that will open this door
   */
  def getValidKeyFromPlayer(player: Player) = {
    player.inventory.getKeys.filter { key =>
      (key.idcolor == idcolor && key.shape == shape)
    }.toList
  }

  override def getItemType = Sprites.door

  override def getParamList = {
    Seq(doorType.toString, IDColor.toString(idcolor), shape)
  }

  override def update(castle: Castle, container: GameContainer, game: StateBasedGame, delta: Int) {
    super.update(castle, container, game, delta)
    doorType match {
      case 0 => sprite.setAnimation("unlocked")
      case 1 => sprite.setAnimation("locked")
      case 2 => sprite.setAnimation("unlocked")
    }

    //    println("distance: " + CastleUtil.distanceBetweenTiles(this.container, castle.player.container))
    //    if (CastleUtil.distanceBetweenTiles(this.container, castle.player.container) > 3) opacity = .25f else opacity = .8f
  }

  override def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {

    if (doorType == 0) {
      sprite.getAnimation.draw(x, y, color)
    }

    if (doorType == 1) {
      sprite.getAnimation.draw(x, y, color)
      keySprite.getAnimation.draw(x + 26, y + 11, 16, 16, idcolor)
    }

    if (doorType == 2) {
      wallSprite.getAnimation.draw(x, y, Color.white)
      sprite.getAnimation.draw(x, y, new Color(color.r, color.g, color.b, opacity))
    }

  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    doorType match {
      case 0 => handleNormalDoor(player)
      case 1 => handleLockedDoor(player)
      case 2 => handleHiddenDoor(player)
    }

    player.attemptMove(MoveDescription(player.lastMove.keyPressed, player.lastMove.animation, 1f))
  }

  private def handleNormalDoor(player: Player) {
    player.attemptMove(MoveDescription(player.lastMove.keyPressed, player.lastMove.animation, player.lastMove.speedModifier)) //push them to the next tile
  }

  private def handleLockedDoor(player: Player) {
    if (canBeOpenedBy(player)) { //if the player can open this door
      player.inventory.decrementItem(getValidKeyFromPlayer(player)(0)) //can assume (0) because canBeOpenedBy will always be true

      doorType = 0 //unlock the door
      handleNormalDoor(player) //continue on as though this were a normal door
    } else { //if the player can't open this door
      player.attemptMove(player.reverseMove(player.lastMove)) //push the player back where they came from
    }
  }

  private def handleHiddenDoor(player: Player) = handleNormalDoor(player)

}