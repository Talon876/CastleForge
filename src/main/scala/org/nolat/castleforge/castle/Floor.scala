package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.castle.items.Pusher
import org.nolat.castleforge.castle.items.attributes.Direction

class Floor(var item: Option[Item], val x: Int, val y: Int) extends Renderable with PlayerListener {
  var sprite = new Sprite(Sprites.floor)
  sprite.setRandomAnimation(List(0.0f, 1f, 0.0f, 0.0f))

  var translate: (Int, Int) => ((Int, Int)) = null //transform

  def isBlockingMovement: Boolean = {
    item match {
      case Some(i) => i.isBlockingMovement
      case None => false
    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    item match {
      case Some(theItem) => theItem.update(container, game, delta)
      case None => //nothing
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    val newX = x * Config.TileWidth + Config.TileOffsetX
    val newY = y * Config.TileHeight + Config.TileOffsetY

    sprite.getAnimation.draw(newX + translate(newX, newY)._1, newY + translate(newX, newY)._2)

    item match {
      case Some(i) => i.render(newX + translate(newX, newY)._1, newY + translate(newX, newY)._2, container, game, g)
      case None => //don't draw
    }
  }

  //this is the default transform method, the one being called is in Castle
  private def transform(x: Int, y: Int): (Int, Int) = {
    (0, 0)
  }

  def itemName = {
    item match {
      case Some(item) => item.getItemType
      case None => "None"
    }
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    item match {
      case Some(item) => item.onPlayerEnter(player, srcFloor)
      case None =>
    }
  }

  override def onPlayerExit(player: Player, destFloor: Floor) {
    item match {
      case Some(item) => item.onPlayerExit(player, destFloor)
      case None =>
    }
  }

  def getTilePosition = (x, y)

}
