package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import scala.collection.mutable.ListBuffer

class Castle(origState: ArrayBuffer[ArrayBuffer[Floor]]) extends Renderable {
  var name: String = "Default"
  var authorName: String = "Default Name"
  var description: String = ""
  var roomLayout: ArrayBuffer[ArrayBuffer[Int]] = new ArrayBuffer()
  var rows: Int = org.nolat.castleforge.Config.DefaultCastleSize._1
  var cols: Int = org.nolat.castleforge.Config.DefaultCastleSize._2
  val originalState: ArrayBuffer[ArrayBuffer[Floor]] = origState
  var inventory: Inventory = new Inventory()
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
  var player: Player = null

  map = originalState.clone //needed to set initial map
  //TODO: check to make sure that a change to map(0)(0) floor object does not change originalMap

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
    getFloorsToRender.foreach(_.render(container, game, g))
    g.drawRect(Config.TileOffsetX, Config.TileOffsetY, 11 * 64, 11 * 64)
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