package org.nolat.castleforge.states

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.AngelCodeFont
import org.newdawn.slick.Input
import org.newdawn.slick.state.transition.CrossStateTransition
import org.newdawn.slick.state.GameState
import org.newdawn.slick.state.transition.EmptyTransition
import org.nolat.castleforge.Config
import org.nolat.castleforge.tools.CodeTimer

object TitleScreen {
  val ID = 1
}
class TitleScreen extends BasicGameState {

  var game: StateBasedGame = null

  override def getID = TitleScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
    Config.init()
    CodeTimer.finish()
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.TitleScreenBackground.draw(0, 0)
    Config.UIFont.drawString(600, 300, "Press enter")
  }

  override def keyReleased(key: Int, c: Char) {
    println(key + " " + c)
    if (key == Input.KEY_RETURN) {

      game.enterState(MainMenuScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    }
  }
}