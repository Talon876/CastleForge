package org.nolat.castleforge.states

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.nolat.castleforge.Config
import org.nolat.castleforge.ui.Menu
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition
import org.nolat.castleforge.ui.HUD

object MainMenuScreen {
  val ID = 2
}

class MainMenuScreen extends BasicGameState {
  override def getID = MainMenuScreen.ID

  var game: StateBasedGame = null

  val mainMenu = new Menu(new Vector2f(500, 300))
  mainMenu.add("Choose Castle", handleMenu)
  mainMenu.add("Build New Castle", handleMenu)
  //mainMenu.add("Download Castles", handleMenu)
  mainMenu.add("Exit", handleMenu)

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    mainMenu.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.stonewall.draw(0, 0)
    HUD.logo.draw(Config.Resolution.getX / 2 - HUD.logo.getWidth / 2, 40)
    mainMenu.render(container, game, g)
  }

  private def handleMenu(text: String, position: Vector2f) = {
    text match {
      case "Choose Castle" => game.enterState(CastleSelectScreen.ID, null, null)
      case "Build New Castle" => game.enterState(CreateCastleScreen.ID, null, null)
      //case "Download Castles" =>
      case "Exit" => sys.exit
    }
  }

}