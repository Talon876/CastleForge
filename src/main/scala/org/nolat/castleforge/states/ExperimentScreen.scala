package org.nolat.castleforge.states

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.ui.Menu

object ExperimentScreen {
  val ID = 3
}
class ExperimentScreen extends BasicGameState {

  var game: StateBasedGame = null

  val menuExample = new Menu(new Vector2f(300, 300))
  menuExample.add("Option 1")
  menuExample.add("Option 2")
  menuExample.add("Something else")
  menuExample.add("Exit")

  override def getID = ExperimentScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    menuExample.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    g.drawString("Experiment Screen", 0, 0)
    Config.crystalBallSprite.draw(100, 100, Color.green)
    Config.crystalBallSprite.getSprite(2, 0).draw(100, 200, Color.cyan)
    Config.testAnim.draw(100, 300, Color.red)
    Config.teleAnim.draw(100, 400)
    menuExample.render(container, game, g)
  }

  override def keyReleased(key: Int, c: Char) {

  }
}