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
object ExperimentScreen2 {
  val ID = 4
}
class ExperimentScreen2 extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen.ID

  lazy val castle = MapLoad.loadMap(new File("/Users/talon/.castleforge/maps/Allitems.xml"), false)

  lazy val tiles = List(
    new Floor(None, 0, 0),
    new Floor(Item("door", List("0")), 1, 0),
    new Floor(Item("door", List("1", "red", "diamond")), 2, 0),
    new Floor(Item("checkpoint", List("false")), 3, 0),
    new Floor(Item("checkpoint", List("true")), 4, 0),
    new Floor(Item("crystal_ball"), 5, 0),
    new Floor(Item("endpoint", List("this is the end message")), 6, 0),
    new Floor(Item("ice"), 7, 0),
    new Floor(Item("key", List("red", "pentagon", "1")), 8, 0),
    new Floor(Item("match", List("5")), 9, 0),
    new Floor(Item("obstacle", List("hole")), 10, 0),
    new Floor(Item("pusher", List("north")), 0, 1),
    new Floor(Item("pusher", List("south")), 1, 1),
    new Floor(Item("sign", List("this is the sign message")), 2, 1),
    new Floor(Item("spawnpoint", List("true")), 3, 1),
    new Floor(Item("teleporter", List("0", "green")), 4, 1),
    new Floor(Item("teleporter", List("1", "blue")), 5, 1),
    new Floor(Item("teleporter", List("2", "red")), 6, 1),
    new Floor(Item("torch", List("true", "medium", "yellow")), 7, 1),
    new Floor(Item("ice"), 8, 1),
    new Floor(Item("ice"), 9, 1),
    new Floor(Item("ice"), 10, 1),
    new Floor(Item("ice"), 0, 2))

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    castle.update(container, game, delta)
    //tiles.foreach(_.update(container, game, delta))

    if (container.getInput().isKeyPressed(Input.KEY_1)) {
    } else if (container.getInput().isKeyPressed(Input.KEY_2)) {

    } else if (container.getInput().isKeyPressed(Input.KEY_3)) {

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    g.setBackground(Color.black)
    g.setColor(Color.white)
    castle.render(container, game, g)
    //tiles.foreach(_.render(container, game, g))

  }

  override def keyReleased(key: Int, c: Char) {

  }
}