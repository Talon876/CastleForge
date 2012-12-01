package org.nolat.castleforge.ui
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Image
import scala.collection.mutable.ListBuffer
import org.nolat.castleforge.tools.Lerper
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.Graphics
import org.nolat.castleforge.ui.editor.TextOptionPane

object HUD {

  var border: Image = null
  var grooves: Image = null
  var logo: Image = null
  var custom: Image = null
  var sign: Image = null
  var selector: Image = null
  var save: Image = null
  var saveOver: Image = null
  var back: Image = null
  var backOver: Image = null

  def init() = {
    border = new Image("images/interface/borders.png")
    grooves = new Image("images/interface/grooves.png")
    logo = new Image("images/interface/logo.png")
    custom = new Image("images/nothing.png")
    sign = new Image("images/interface/sign.png")
    selector = new Image("images/interface/selector.png")
    save = new Image("images/interface/save.png")
    saveOver = new Image("images/interface/saveOver.png")
    back = new Image("images/interface/back.png")
    backOver = new Image("images/interface/backOver.png")
  }
}

class HUD extends Renderable {

  var position: Vector2f = new Vector2f(0f, 0f)
  var offset: Vector2f = new Vector2f(0f, 0f)
  val elements = new ListBuffer[HUDElement]

  def enter(container: GameContainer, game: StateBasedGame) {
    TextOptionPane.textfieldInFocus = false
    elements.foreach(_.enter(container, game))
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    elements.foreach(_.update(container, game, delta))
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    elements.foreach(_.render(container, game, g))
  }

  def add(element: HUDElement) = elements += element

}