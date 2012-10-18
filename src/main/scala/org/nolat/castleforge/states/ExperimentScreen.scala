package org.nolat.castleforge.states

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.ui.Menu
import org.nolat.castleforge.graphics.Loader
import org.newdawn.slick.Input
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import scala.collection.mutable.MutableList
import org.nolat.castleforge.xml.MapLoad
import java.io.File

object ExperimentScreen {
  val ID = 3
}
class ExperimentScreen extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen.ID

  lazy val castle = MapLoad.loadMap(new File("/Users/talon/.castleforge/maps/ExampleMap.xml"), false)

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    castle.update(container, game, delta)
    if (container.getInput().isKeyPressed(Input.KEY_1)) {

    } else if (container.getInput().isKeyPressed(Input.KEY_2)) {

    } else if (container.getInput().isKeyPressed(Input.KEY_3)) {

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    g.setBackground(Color.black)
    g.setColor(Color.white)
    castle.render(container, game, g)

  }

  override def keyReleased(key: Int, c: Char) {

  }
}