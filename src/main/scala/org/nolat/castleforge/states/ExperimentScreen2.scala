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
import org.nolat.castleforge.castle.Player

object ExperimentScreen2 {
  val ID = 4
}
class ExperimentScreen2 extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen2.ID

  lazy val castle = MapLoad.loadMap(new File("/Users/talon/.castleforge/maps/Allitems.xml"), false)

  var player: Player = null

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
    this.player = new Player
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    castle.update(container, game, delta)
    player.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    castle.render(container, game, g)
    player.render(container, game, g)
    //g.setColor(Color.green)
    //g.drawRect(player._1 * Config.TileWidth + Config.TileOffsetX, player._2 * Config.TileHeight + Config.TileOffsetY, 64, 64)
  }

  override def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      game.enterState(ExperimentScreen2.ID, new EmptyTransition(), new EmptyTransition())
    }
  }
}