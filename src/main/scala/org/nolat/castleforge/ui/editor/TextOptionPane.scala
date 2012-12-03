package org.nolat.castleforge.ui.editor

import org.newdawn.slick.GameContainer
import org.nolat.castleforge.Config
import org.newdawn.slick.gui.TextField
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.Font
object TextOptionPane {
  var textfieldInFocus = false
}
class TextOptionPane(container: GameContainer, x: Int, val y: Int, parent: ElementToolOptions) extends OptionPane(parent) {

  lazy val textfield = {
    val t = new LoggedTextField(container, Config.guiFont, x + 20, Config.Resolution.getY + 1, 64 * 7 - 32, 24)
    t.setFocus(false)
    t
  }

  private var lastFocusState = true

  override def reset() = {
    // println("hiding textfield")
    textfield.setLocation(textfield.getX(), Config.Resolution.getY + 1)
    textfield.setText("")
  }

  def hidden = textfield.getY > Config.Resolution.getY

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

    if (textfield.hasFocus && lastFocusState == false) {
      //("textfield got focus")
      TextOptionPane.textfieldInFocus = true
    } else if (!textfield.hasFocus && lastFocusState == true) {
      //println("textfield lost focus")
      TextOptionPane.textfieldInFocus = false
    }

    if (!hidden && container.isPaused) { //menu is up, so lose focus
      textfield.setFocus(false)
      TextOptionPane.textfieldInFocus = false
      //println("textfield lost focus because it's hidden and container is paused")
    } else if (!hidden && !container.isPaused) { //not hidden and no longer paused, give focus back
      textfield.setFocus(true)
      TextOptionPane.textfieldInFocus = true
      //println("textfiled gained focus because it's not hidden and container isn't paused")
    }

    lastFocusState = textfield.hasFocus
  }

  override def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setColor(Color.white)
    textfield.render(container, g)
  }

  class LoggedTextField(container: GameContainer, font: Font, x: Int, y: Int, width: Int, height: Int) extends TextField(container, font, x, y, width, height) {

    override def keyPressed(key: Int, c: Char) {
      if (this.hasFocus) {
        super.keyPressed(key, c)
        parent.onOptionsChanged(List(textfield.getText))
      };
    }
  }

}

