package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.nolat.castleforge.graphics.Renderable
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Torch
import org.nolat.castleforge.castle.items.Wall

class Castle(origState: ArrayBuffer[ArrayBuffer[Floor]], curState: ArrayBuffer[ArrayBuffer[Floor]]) extends Renderable {
  var name: String = "Default"
  var authorName: String = "Default Name"
  var description: String = ""
  val originalState: ArrayBuffer[ArrayBuffer[Floor]] = origState
  
  private var _map: ArrayBuffer[ArrayBuffer[Floor]] = curState
  def map = _map
  def map_=(mp: ArrayBuffer[ArrayBuffer[Floor]]) = {
    _map = mp;
    //update all Floors in map so that they use this classes translate
    updateFloorTransitions()
  }
  
  private val _player: Player = new Player(this)
  def player = _player
  private val _inventory: Inventory = player.inventory
  def inventory = _inventory
  def inventory_=(inv: Inventory)
  {
    player.inventory.clear()
    player.inventory.addItems(inv.flatten : _*)
  }
  val lighting = new Lighting(this)

  updateFloorTransitions()
  
  def this() {
    this(new ArrayBuffer[ArrayBuffer[Floor]], new ArrayBuffer[ArrayBuffer[Floor]])
  }

  def this(origState: ArrayBuffer[ArrayBuffer[Floor]], curState: ArrayBuffer[ArrayBuffer[Floor]],  nam: String, authorNam: String, descript: String) {
    this(origState, curState)
    name = nam
    authorName = authorNam
    description = descript

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    lighting.update()
    map.flatten.foreach(_.update(this, container, game, delta))
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
    tempList.filter(floor =>
      floor.sharesRoomId(player.container)).toList
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

  def updateFloorTransitions()
  {
    map.foreach { row =>
      row.foreach { floor =>
        floor.translate = translate
      }
    }
  }
}