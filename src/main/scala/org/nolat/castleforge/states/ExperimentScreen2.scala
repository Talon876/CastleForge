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
import org.nolat.castleforge.tools.Lerper
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.ui.HUDElement
import org.nolat.castleforge.ui.ElementPlayerDebug
import org.nolat.castleforge.ui.ElementInventory

object ExperimentScreen2 {
  val ID = 4
}
class ExperimentScreen2 extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen2.ID

  var castle: Castle = null

  var player: Player = null

  val hud = new HUD()
  var playerDebug: ElementPlayerDebug = null

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    println("Entered Experiment screen")
    castle = MapLoad.loadMap(new File("/Users/talon/.castleforge/maps/large.xml"), false)
    this.player = new Player(castle)
    this.castle.player = player

    val borders = new HUDElement(HUD.border)
    hud add borders

    val grooves = new HUDElement(HUD.grooves)
    grooves.position = new Vector2f(728, 8)
    hud add grooves

    val logo = new HUDElement(HUD.logo)
    logo.position = new Vector2f(728, 588)
    hud add logo

    playerDebug = new ElementPlayerDebug(player)
    playerDebug.position = new Vector2f(8, 8 + 64 * 9)
    hud add playerDebug

    val playerInventory = new ElementInventory(player)
    playerInventory.position = new Vector2f(grooves.position.x + 16, grooves.position.y + 16)
    hud add playerInventory
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    Lerper.lerpers.foreach(_.update(delta))
    castle.update(container, game, delta)
    player.update(container, game, delta)
    hud.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    castle.render(container, game, g)
    player.render(container, game, g)
    g.setColor(Color.black)
    hud.render(container, game, g)
  }

  override def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      game.enterState(ExperimentScreen2.ID, new EmptyTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F3) {
      playerDebug.toggle()
    }
  }
}