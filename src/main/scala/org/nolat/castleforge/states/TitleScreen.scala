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
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.xml.MapSave

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
    MapSave.dummyInit //Making sure the MapSave singleton is initialized before the game loop runs
    CodeTimer.finish()
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    //Moved input checking into update because isKeyPressed caches if a key is pressed since the last check of isKeyPressed
    //Since the rest of the menus use this keyReleased does not reset isKeyPressed and causes a the next menu to think enter has been pressed
    if (container.getInput().isKeyPressed(Input.KEY_ENTER)) {
      game.enterState(MainMenuScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))

    } else if (container.getInput().isKeyPressed(Input.KEY_F9)) {
      //SharedStateData.loadOriginal = false
      //SharedStateData.mapFile = new File(Config.WorkingDirectory + "/maps/talon-everything.xml")
      //game.enterState(CastleLoading.ID, new FadeOutTransition(Color.black), new EmptyTransition())
      game.enterState(ExperimentScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))
    } else if (container.getInput().isKeyPressed(Input.KEY_F10)) {
      SharedStateData.loadOriginal = true
      SharedStateData.loadedCastle = new File(Config.WorkingDirectory + "/maps/talon-everything.xml")
      game.enterState(CastleLoading.ID, new FadeOutTransition(Color.black), new EmptyTransition())
    }
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
    Config.stonewall.draw(0, 0)
    HUD.logo.draw(Config.Resolution.getX / 2 - HUD.logo.getWidth / 2, 40)
    Config.UIFont.drawString(Config.Resolution.getX / 2 - 100, Config.Resolution.getY - 100, "Press enter", new Color(0, 0, 0, alpha))
  }

  override def keyReleased(key: Int, c: Char) {

  }
}