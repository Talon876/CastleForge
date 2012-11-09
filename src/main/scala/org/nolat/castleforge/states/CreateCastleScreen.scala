package org.nolat.castleforge.states

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition
import org.nolat.castleforge.Config
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.gui.TextField
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.Color

object CreateCastleScreen {
  val ID = 7
}

class CreateCastleScreen extends BasicGameState with ComponentListener {
  var game: StateBasedGame = null
  override def getID = CreateCastleScreen.ID

  var authorText: TextField = null
  var castleText: TextField = null

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

    authorText = new TextField(container, Config.guiFont, 450, 300, 300, 22, this)
    castleText = new TextField(container, Config.guiFont, 450, authorText.getY + authorText.getHeight + 8, 300, 22, this)
  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    println("entered create castle")
    container.setDefaultMouseCursor()

    authorText.setText("")
    authorText.setFocus(true)
    castleText.setText("")
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setColor(Color.white)
    g.setFont(Config.guiFont)
    Config.TitleScreenBackground.draw(0, 0)
    authorText.render(container, g)
    castleText.render(container, g)
    g.setColor(Color.black)
    g.drawString("        Author: ", authorText.getX - 115, authorText.getY)
    g.drawString("Castle Name: ", castleText.getX - 115, castleText.getY)
  }

  override def keyReleased(key: Int, c: Char) {
    key match {
      case Input.KEY_BACK => //game.enterState(MainMenuScreen.ID, new FadeOutTransition(), new FadeInTransition())
      case Input.KEY_TAB => handleTab()
      case _ =>
    }
  }

  def handleTab() = {
    if (authorText.hasFocus) {
      castleText.setFocus(true)
    } else if (castleText.hasFocus) {
      authorText.setFocus(true)
    }
  }

  override def componentActivated(source: AbstractComponent) {

  }
}