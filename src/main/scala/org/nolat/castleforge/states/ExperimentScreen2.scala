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
import org.nolat.castleforge.ui.ElementSign
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Torch
import org.nolat.castleforge.castle.items.Pusher
import org.nolat.castleforge.ui.MainHUD
import org.newdawn.slick.state.transition.RotateTransition

object ExperimentScreen2 {
  val ID = 4
}
class ExperimentScreen2 extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen2.ID

  var castle: Castle = null

  var hud: MainHUD = null

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def enter(container: GameContainer, game: StateBasedGame) {

    if (SharedStateData.mapFile == null) { //hasn't loaded from the File
      var file = new File("/Users/talon/.castleforge/maps/talon-lighting.xml")
      if (file.exists) {
        castle = MapLoad.loadMap(file, false)
      } else {
        castle = MapLoad.loadMap(new File("C:/test.xml"), false)
      }
    } else {
      castle = SharedStateData.loadedCastle //grab loaded castle
    }

    hud = new MainHUD(castle, game)
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
    } else {
      g.setColor(Color.white)
      g.drawString("Loading...", 1280 / 2, 720 / 2)
    }
  }

  override def keyReleased(key: Int, c: Char) {
    hud.keyReleased(key, c)

    if (key == Input.KEY_F9) {
      game.enterState(ExperimentScreen.ID, new RotateTransition(), new EmptyTransition())
    } else if (key == Input.KEY_F10) {
      game.enterState(ExperimentScreen2.ID, new EmptyTransition(), new EmptyTransition())
    }
  }
}