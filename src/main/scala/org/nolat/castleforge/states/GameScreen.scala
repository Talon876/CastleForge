package org.nolat.castleforge.states

import java.io.File
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.transition.EmptyTransition
import org.newdawn.slick.state.transition.RotateTransition
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.tools.Lerper
import org.nolat.castleforge.ui.MainHUD
import org.nolat.castleforge.xml.MapLoad
import org.nolat.castleforge.Config

object GameScreen {
  val ID = 6
}

class GameScreen extends BasicGameState {
  var game: StateBasedGame = null
  override def getID = GameScreen.ID

  var castle: Castle = null
  var hud: MainHUD = null

  var fadingIn = false
  var opacity = 1f

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    castle = SharedStateData.loadedCastle
    hud = new MainHUD(castle, game)
    fadingIn = true
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (!container.isPaused) {
      Lerper.lerpers.foreach(_.update(delta))
      castle.update(container, game, delta)
      castle.player.update(container, game, delta)
    }

    if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.setPaused(!container.isPaused)
    hud.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (castle != null) {
      g.setBackground(Color.black)
      g.setColor(Color.white)
      castle.render(container, game, g)
      castle.player.render(container, game, g)
      g.setColor(Color.black)
      hud.render(container, game, g)
      handleFade(g)
    }
  }

  private def handleFade(g: Graphics) {
    if (fadingIn) {
      g.setColor(new Color(0, 0, 0, opacity))
      g.fillRect(0, 0, Config.Resolution.getX, Config.Resolution.getY)
      opacity -= .01f
      if (opacity < 0f) {
        fadingIn = false
        opacity = 1f
      }
    }
  }

  override def keyReleased(key: Int, c: Char) {
    hud.keyReleased(key, c)

    if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      game.enterState(ExperimentScreen2.ID, new EmptyTransition(), new EmptyTransition())
    }
  }

}