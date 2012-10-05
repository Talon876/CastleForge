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

object ExperimentScreen {
  val ID = 3
}
class ExperimentScreen extends BasicGameState {

  var game: StateBasedGame = null

  override def getID = ExperimentScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
    Config
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
      Config.sprites("teleporter").setAnimation("bidirectional")
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    g.drawString("Experiment Screen", 5, 10)
    Config.sprites(Sprites.teleporter).getAnimation().draw(100, 100)
    Config.sprites(Sprites.teleporter).setAnimation("bidirectional")
    Config.sprites(Sprites.teleporter).getAnimation().draw(100, 200)
  }

  override def keyReleased(key: Int, c: Char) {

  }
}