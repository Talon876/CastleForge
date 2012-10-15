package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config

class Castle(origState: ArrayBuffer[ArrayBuffer[Floor]]) extends Renderable {
  var name: String = "Default"
  var authorName: String = "Default Name"
  var description: String = ""
  var roomLayout: ArrayBuffer[ArrayBuffer[Int]] = new ArrayBuffer()
  var rows: Int = org.nolat.castleforge.Config.DefaultCastleSize._1
  var cols: Int = org.nolat.castleforge.Config.DefaultCastleSize._2

  val originalState: ArrayBuffer[ArrayBuffer[Floor]] = origState
  var map: ArrayBuffer[ArrayBuffer[Floor]] = new ArrayBuffer()
  var inventory: Inventory = new Inventory()

  val tempPlayer = (3, 3)

  def this(origState: ArrayBuffer[ArrayBuffer[Floor]], nam: String, authorNam: String, descript: String) {
    this(origState)
    name = nam
    authorName = authorNam
    description = descript
    //update all Floors in map so that they use this classes translate
    map.foreach { row =>
      row.foreach { floor =>
        floor.translate = translate
      }

    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    getFloorsToRender.foreach(_.render(container, game, g))
    g.drawRect(Config.TileOffsetX, Config.TileOffsetY, 11 * 64, 11 * 64)
  }

  def getFloorsToRender(): List[Floor] = {
    map.flatten.toList //List(map(0)(0))
  }

  def translate(x: Int, y: Int): (Int, Int) = {
    (x, y)
  }
}