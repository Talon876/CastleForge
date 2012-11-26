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
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.ExpansionDirection

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
    container.setDefaultMouseCursor()
    castle = Castle(generateBlankCastle, null)
    CastleUtil.removeItem(castle, (2, 2))
    CastleUtil.expandCastle(castle, ExpansionDirection.ALL, 3)
    castle.isEditor = true
    hud = new EditorHUD(castle, container)
    hud.enter(container, game)
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
      case _ =>
    }
  }

  private def generateBlankCastle(): ArrayBuffer[ArrayBuffer[Floor]] = {
    var map = new ArrayBuffer[ArrayBuffer[Floor]]()

    (0 to 4).foreach { col =>
      var row = new ArrayBuffer[Floor]
      (0 to 4).foreach { r =>
        if (col == 0 || col == 4 || r == 0 || r == 4) {
          row += new Floor(Item("wall"), r, col, "0,1")
        } else {
          row += new Floor(None, r, col, "1")
        }

      }
      map += row
    }
    //    map(145)(150).item = Item("checkpoint", List("false"))
    //    map(150)(145).item = Item("checkpoint", List("false"))
    //    map(155)(150).item = Item("checkpoint", List("false"))
    //    map(150)(155).item = Item("checkpoint", List("false"))
    map(2)(2).item = Item("spawnpoint", List("true"))
    //map(145)(145).item = Item("torch", List("false", "high", "white"))

    //    (1 to 10).foreach { i =>
    //      val point = (Config.random.nextInt(30), Config.random.nextInt(30))
    //      val point2 = (Config.random.nextInt(30), Config.random.nextInt(30))
    //      map(point._1)(point._2).item = Item("ice")
    //      map(point2._1)(point2._2).item = Item("wall")
    //    }
    map
  }
}