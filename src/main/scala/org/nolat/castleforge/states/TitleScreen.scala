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
import org.newdawn.slick.state.transition.SelectTransition
import org.newdawn.slick.state.transition.BlobbyTransition
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition
import java.io.File

object TitleScreen {
  val ID = 1
}
class TitleScreen extends BasicGameState {

  var game: StateBasedGame = null

  override def getID = TitleScreen.ID

  var alpha = 1f
  var decreasing = true

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
    Config.init()
    CodeTimer.finish()
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (decreasing) {
      alpha -= .01f
      if (alpha <= .3f) {
        alpha = .3f
        decreasing = false
      }
    } else {
      alpha += .01f
      if (alpha >= 1f) {
        alpha = 1f
        decreasing = true
      }
    }

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.TitleScreenBackground.draw(0, 0)
    Config.UIFont.drawString(Config.Resolution.getX / 2 - 100, Config.Resolution.getY - 100, "Press enter", new Color(0, 0, 0, alpha))
  }

  override def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_RETURN) {

      game.enterState(MainMenuScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))

    } else if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      SharedStateData.loadOriginal = false
      SharedStateData.mapFile = new File(Config.WorkingDirectory + "/maps/talon-everything.xml")
      game.enterState(CastleLoading.ID, new FadeOutTransition(Color.black), new EmptyTransition())
    }
  }
}