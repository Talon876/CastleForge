package org.nolat.castleforge.states

import scala.collection.mutable.ArrayBuffer
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.ui.EditorHUD
import org.nolat.castleforge.tools.Lerper
import org.nolat.castleforge.Config

object CreateCastleScreen {
  val ID = 7
}

class CreateCastleScreen extends BasicGameState {
  var game: StateBasedGame = null
  override def getID = CreateCastleScreen.ID

  var castle: Castle = null
  var hud: EditorHUD = null

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    println("entered create castle")
    container.setDefaultMouseCursor()
    castle = Castle(generateBlankCastle, null)
    castle.isEditor = true
    hud = new EditorHUD(castle, container)
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    Lerper.lerpers.foreach(_.update(delta))
    castle.update(container, game, delta)
    hud.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    castle.render(container, game, g)
    g.setColor(Color.black)
    hud.render(container, game, g)
  }

  override def keyReleased(key: Int, c: Char) {
    hud.keyReleased(key, c)
    key match {
      case Input.KEY_BACK => //game.enterState(MainMenuScreen.ID, new FadeOutTransition(), new FadeInTransition())
      case Input.KEY_TAB =>
      case _ =>
    }
  }

  private def generateBlankCastle(): ArrayBuffer[ArrayBuffer[Floor]] = {
    var map = new ArrayBuffer[ArrayBuffer[Floor]]()

    (0 to 300).foreach { col =>
      var row = new ArrayBuffer[Floor]
      (0 to 300).foreach { r =>
        row += new Floor(None, r, col, "0")
      }
      map += row
    }
    map(145)(150).item = Item("checkpoint", List("false"))
    map(150)(145).item = Item("checkpoint", List("false"))
    map(155)(150).item = Item("checkpoint", List("false"))
    map(150)(155).item = Item("checkpoint", List("false"))
    map(150)(150).item = Item("spawnpoint", List("true"))

    (1 to 1000).foreach { i =>
      val point = (Config.random.nextInt(300), Config.random.nextInt(300))
      val point2 = (Config.random.nextInt(300), Config.random.nextInt(300))
      map(point._1)(point._2).item = Item("ice")
      map(point2._1)(point2._2).item = Item("wall")
    }
    map
  }
}