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
import org.newdawn.slick.Color

class Floor(private var _item: Option[Item], val x: Int, val y: Int, val roomIDs: String = "0") extends PlayerListener {
  updateFloor()

  def item = _item
  def item_=(itm: Option[Item]) {
    _item = itm
    updateFloor()
  }

  var sprite = new Sprite(Sprites.floor)
  sprite.setAnimation("type" + ((roomIDs.split(",")(0).toInt % 2) + 1))
  //sprite.setRandomAnimation(List(0.0f, 1f, 0.0f, 0.0f))

  var darkness = 0.15f

  var translate: (Int, Int) => ((Int, Int)) = null

  def isBlockingMovement: Boolean = {
    item match {
      case Some(i) => i.isBlockingMovement
      case None => false
    }
  }

  def update(castle: Castle, container: GameContainer, game: StateBasedGame, delta: Int) {
    item match {
      case Some(theItem) => theItem.update(castle, container, game, delta)
      case None => //nothing
    }
  }

  def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    val newX = x * Config.TileWidth + Config.TileOffsetX
    val newY = y * Config.TileHeight + Config.TileOffsetY

    sprite.getAnimation.draw(newX + translate(newX, newY)._1, newY + translate(newX, newY)._2)

    item match {
      case Some(i) => i.render(newX + translate(newX, newY)._1, newY + translate(newX, newY)._2, container, game, g)
      case None => //don't draw
    }

    //draw darkness
    g.setColor(new Color(0, 0, 0, darkness))
    g.fillRect(newX + translate(newX, newY)._1, newY + translate(newX, newY)._2, Config.TileWidth, Config.TileHeight)
  }

  private def updateFloor() = {
    item match {
      case Some(itm) => itm.container = this
      case None =>
    }
  }

  //this is the default transform method, the one being called is in Castle
  private def transform(x: Int, y: Int): (Int, Int) = {
    (0, 0)
  }

  def itemName = {
    item match {
      case Some(item) => item.getItemType
      case None => "Floor"
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

  def sharesRoomId(other: Floor): Boolean = sharesRoomId(other.roomIDs)

  def sharesRoomId(otherIDs: String, exact: Boolean): Boolean = {
    if (exact) {
      roomIDs == otherIDs
    } else {
      val ids = roomIDs.split(",").toList
      val otherIds = otherIDs.split(",").toList
      ids.intersect(otherIds).size > 0
    }

  }

  def sharesRoomId(otherIDs: String): Boolean = sharesRoomId(otherIDs, false)

  override def toString() = "Floor(" + roomIDs + ", " + getTilePosition + ", " + itemName + ")\n"

}
