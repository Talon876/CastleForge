package org.nolat.castleforge.states
import org.newdawn.slick.state.transition.EmptyTransition
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
import org.nolat.castleforge.ui.CastleSelectMenu
import org.newdawn.slick.Input
import java.io.File
import org.newdawn.slick.state.transition.EmptyTransition
import org.nolat.castleforge.ui.HUD

object CastleSelectScreen {
  val ID = 9
}

class CastleSelectScreen extends BasicGameState {
  override def getID = CastleSelectScreen.ID

  var game: StateBasedGame = null

  var castleMenu = new CastleSelectMenu(new Vector2f(100, 100))

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    val mapsFolderStr: String = Config.WorkingDirectory + "/maps"
    val mapsFolder: File = new File(mapsFolderStr)
    mapsFolder.mkdirs()
    castleMenu = new CastleSelectMenu(new Vector2f(100, 100))
    val files = mapsFolder.listFiles().filter(f => f.getName().substring(f.getName().length - 3) == "map")
    files.foreach { f =>
      castleMenu.add(f, handleMenu)
    }
  }
  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.getInput().isKeyPressed(Input.KEY_ESCAPE) || container.getInput().isKeyPressed(Input.KEY_BACK)) {
      game.enterState(MainMenuScreen.ID, null, null)
    } else {
      castleMenu.update(container, game, delta)
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.stonewall.draw(0, 0)
    Config.UIFont.drawString(200, 20, "Castle Selection", Color.black)
    castleMenu.render(container, game, g)
  }

  private def handleMenu(file: File, key: Int) = {
    key match {
      case Input.KEY_E => {
        SharedStateData.loadOriginal = true
        SharedStateData.mapFile = file
        SharedStateData.screenToLoad = CreateCastleScreen.ID
        game.enterState(CastleLoading.ID, new EmptyTransition(), new EmptyTransition())
      }
      case Input.KEY_ENTER => {
        SharedStateData.loadOriginal = false
        SharedStateData.mapFile = file
        game.enterState(CastleLoading.ID, new EmptyTransition(), new EmptyTransition())
      }
    }
  }

}