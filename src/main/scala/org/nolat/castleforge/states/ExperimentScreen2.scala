package org.nolat.castleforge.states

import org.newdawn.slick.state.BasicGameState
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
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.items.Item
import org.newdawn.slick.state.transition.EmptyTransition
object ExperimentScreen2 {
  val ID = 4
}
class ExperimentScreen2 extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen2.ID

  lazy val castle = MapLoad.loadMap(new File("/Users/talon/.castleforge/maps/Allitems.xml"), false)

  var player = (3, 3)

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    castle.update(container, game, delta)

    if (container.getInput().isKeyPressed(Input.KEY_W)) {
      player = (player._1 + 0, player._2 - 1)
    } else if (container.getInput().isKeyPressed(Input.KEY_S)) {
      player = (player._1 + 0, player._2 + 1)
    } else if (container.getInput().isKeyPressed(Input.KEY_A)) {
      player = (player._1 - 1, player._2 - 0)
    } else if (container.getInput().isKeyPressed(Input.KEY_D)) {
      player = (player._1 + 1, player._2 - 0)
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    castle.render(container, game, g)
    g.setColor(Color.green)
    g.drawRect(player._1 * Config.TileWidth + Config.TileOffsetX, player._2 * Config.TileHeight + Config.TileOffsetY, 64, 64)
  }

  override def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      game.enterState(ExperimentScreen2.ID, new EmptyTransition(), new EmptyTransition())
    }
  }
}