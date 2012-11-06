package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.nolat.castleforge.graphics.Renderable
import org.nolat.castleforge.castle.items.Item

class Castle(origState: ArrayBuffer[ArrayBuffer[Floor]]) extends Renderable {
  var name: String = "Default"
  var authorName: String = "Default Name"
  var description: String = ""
  val originalState: ArrayBuffer[ArrayBuffer[Floor]] = origState
  private var _inventory: Inventory = new Inventory()
  def inventory = _inventory
  def inventory_=(inv: Inventory) = {//TODO: clean up inventory and player setters
    if (player != null) { //Player is set already
      player.inventory.clear()
      player.inventory.addItems(inv.flatten: _*)
      _inventory = player.inventory //pass the reference through

    } else {
      _inventory = inv //Player is not set, load inventory (Default case)
    }
  }
  private var _map: ArrayBuffer[ArrayBuffer[Floor]] = null //position of this line of code matters it will be called again if put below map = originalMap
  def map = _map
  def map_=(mp: ArrayBuffer[ArrayBuffer[Floor]]) = {
    _map = mp;
    //update all Floors in map so that they use this classes translate
    map.foreach { row =>
      row.foreach { floor =>
        floor.translate = translate
      }
    }
  }
  private var _player: Player = null
  def player = _player
  def player_=(plyr: Player) = {
    if (player == null) { //First Player set in this castle
      _player = plyr
      if (inventory != null) //Is set after inventory (Default case)
      {
        player.inventory.addItems(inventory.flatten: _*)
      }
    } else { //There was already a player in this castle
      val temp: Inventory = player.inventory //old inventory
      _player = plyr
      if (inventory != null) { //Already player but no inventory
        player.inventory.clear()
        if (temp != null) { //old player had an inventory
          player.inventory.addItems(temp.flatten: _*)
        }
      }
    }
    _inventory = player.inventory //set the castle inventory to the players inventory reference
  }

  def this() {
    this(new ArrayBuffer[ArrayBuffer[Floor]])
  }

  def this(origState: ArrayBuffer[ArrayBuffer[Floor]], nam: String, authorNam: String, descript: String) {
    this(origState)
    name = nam
    authorName = authorNam
    description = descript

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.backdrop.draw(0, 0)
    getFloorsToRender.foreach(_.render(container, game, g))
  }

  def getFloorsToRender(): List[Floor] = {
    //map.flatten.toList //List(map(0)(0))
    var tempList = new ListBuffer[Floor]
    for (x <- cameraBounds._1 to cameraBounds._2) {
      for (y <- cameraBounds._3 to cameraBounds._4) {
        tempList += map(y)(x)
      }
    }
    tempList.toList
  }

  private def cameraBounds = {
    val camRadius = 6
    val left = scala.math.max(player.tilePosition._1 - camRadius, 0)
    val right = scala.math.min(player.tilePosition._1 + camRadius, map(0).size - 1)
    val top = scala.math.max(player.tilePosition._2 - camRadius, 0)
    val bottom = scala.math.min(player.tilePosition._2 + camRadius, map.size - 1)
    (left, right, top, bottom)
  }

  def translate(x: Int, y: Int): (Int, Int) = {
    //(-player.tileOffset._1 * 64, -player.tileOffset._2 * 64)
    (-player.movementOffset._1.toInt, -player.movementOffset._2.toInt)
  }

  def getFloorAtPosition(tilePosition: (Int, Int)): Floor = {
    getFloorAtPosition(tilePosition._1, tilePosition._2)
  }

  def getFloorAtPosition(x: Int, y: Int): Floor = {
    map(y)(x)
  }

  def getFloorAtPositionWithOffset(tilePosition: (Int, Int), offset: (Int, Int)): Floor = {
    val position = (tilePosition._1 + offset._1, tilePosition._2 + offset._2)
    getFloorAtPosition(position)
  }
}