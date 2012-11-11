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

  def init() = {
    addroom = new Image("images/tools/roomadd.png")
    deleteroom = new Image("images/tools/roomdelete.png")
    eraser = new Image("images/tools/eraser.png")
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

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    area.render(container, g)
  }

  override def componentActivated(source: AbstractComponent) {
    onClick(this)
  }
}