package org.nolat.castleforge.states

import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color

object MainMenuScreen {
  val ID = 2
}

class MainMenuScreen extends BasicGameState {

  override def getID = MainMenuScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setColor(Color.white)
    g.drawString("CastleForge Main Menu", 600, 80)
  }

}