package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.Image
import org.newdawn.slick.gui.MouseOverArea
import org.nolat.castleforge.ui.HUD
import org.newdawn.slick.GameContainer
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.Graphics
object Tool {

  var addroom: Image = null
  var deleteroom: Image = null
  var eraser: Image = null

  var colors: Image = null
  var quantities: Image = null
  var shapes: Image = null
  var directions: Image = null

  var lumens: Image = null
  var teleporterTypes: Image = null
  var doorType: Image = null

  var torchstates: Image = null

  def init() = {
    addroom = new Image("images/tools/roomadd.png")
    deleteroom = new Image("images/tools/roomdelete.png")
    eraser = new Image("images/tools/eraser.png")

    colors = new Image("images/tools/colors.png")
    quantities = new Image("images/tools/quantities.png")
    shapes = new Image("images/tools/shapes.png")
    directions = new Image("images/tools/directions.png")

    lumens = new Image("images/tools/lumens.png")
    teleporterTypes = new Image("images/tools/teleportertypes.png")
    doorType = new Image("images/tools/doortypes.png")

    torchstates = new Image("images/tools/torchstates.png")
  }
}
abstract class Tool(val x: Int, val y: Int, val castle: Castle, container: GameContainer) extends Renderable with ComponentListener {

  var onClick: (Tool) => Unit = { t => }
  lazy val area = {
    val moa = new MouseOverArea(container, HUD.custom, x, y, 64, 64, this)
    moa.setMouseOverImage(HUD.selector)
    moa
  }

  def apply(region: List[List[Floor]])

  def selectionUpdated(region: List[List[Floor]]) {

  }

  def isValidSelection(region: List[List[Floor]]) {

  }

  def getOptions: List[String] = Nil

  def setOptions(options: List[Any]) {}

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    area.render(container, g)
  }

  override def componentActivated(source: AbstractComponent) {
    onClick(this)
  }

}