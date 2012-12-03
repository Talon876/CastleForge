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
import java.io.File
import org.nolat.castleforge.xml.MapSave

object Castle {
  def apply(curState: ArrayBuffer[ArrayBuffer[Floor]], castleName: String = "untitled" + Config.random.nextInt(100000), authorNam: String = "default", descript: String = ""): Castle = {
    val castle = new Castle(curState, castleName, authorNam, descript)
    //MapSave.save(castle, true)
    //TODO: determine if we want to save here for debugging purposes I will leave it for now
    //probably don't want to save here since it'll be saved under default-untitled293842 and then when they go to save manually, it'll get renamed and there will be 2 files. or if they choose not to save it there will be a near empty file saved
    castle
  }
  def getSaveLocation(authorNam: String, castleName: String): File = {
    val mapsFolderStr: String = Config.WorkingDirectory + "/maps"
    val mapsFolder: File = new File(mapsFolderStr)
    mapsFolder.mkdirs()
    new File(mapsFolderStr + "/" + authorNam + "-" + castleName + ".map") //now a .map file
  }
}
class Castle(curState: ArrayBuffer[ArrayBuffer[Floor]]) extends Renderable {
  var name: String = "Default"
  var authorName: String = "Default Name"
  var description: String = ""
  var game: StateBasedGame = null

  private var _map: ArrayBuffer[ArrayBuffer[Floor]] = curState
  def map = _map
  def map_=(mp: ArrayBuffer[ArrayBuffer[Floor]]) = {
    _map = mp;
    //update all Floors in map so that they use this classes translate
    updateFloorTransitions()
    lighting.update()
  }

  private val _player: Player = new Player(this)
  def player = _player
  private val _inventory: Inventory = player.inventory
  def inventory = _inventory
  def inventory_=(inv: Inventory) {
    _inventory.clear()
    _inventory.addItems(inv.flatten: _*)
  }
  val lighting = new Lighting(this)
  lighting.update()
  var isEditor = false

  updateFloorTransitions()

  def this(curState: ArrayBuffer[ArrayBuffer[Floor]], nam: String, authorNam: String, descript: String) {
    this(curState)
    name = nam
    authorName = authorNam
    description = descript

  }

  def fileLocation(): File = {
    Castle.getSaveLocation(authorName, name)
  }
  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    getFloorsToRender.foreach(_.update(this, container, game, delta))
    player.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.backdrop.draw(0, 0)
    getFloorsToRender.foreach(_.render(container, game, g))
    if (!isEditor) player.render(container, game, g)
  }

  def getFloorsToRender(): List[Floor] = {
    var tempList = new ListBuffer[Floor]
    for (x <- cameraBounds._1 to cameraBounds._2) {
      for (y <- cameraBounds._3 to cameraBounds._4) {
        tempList += map(y)(x)
      }
    }
    if (isEditor) tempList.toList else tempList.filter(floor =>
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

  def updateFloorTransitions() {
    map.foreach { row =>
      row.foreach { floor =>
        floor.translate = translate
      }
    }
  }

  def nextRoomId = {
    var maxRoomId = 0
    map.flatten.foreach {
      tile =>
        tile.roomIDlist.foreach { id =>
          if (id > maxRoomId) {
            maxRoomId = id
            //println("new max is " + maxRoomId)
          }
        }
    }
    maxRoomId + 1
  }
}