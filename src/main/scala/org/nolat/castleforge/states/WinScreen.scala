package org.nolat.castleforge.states

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import org.nolat.castleforge.ui.HUD
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.Input
import org.newdawn.slick.Color
import org.newdawn.slick.state.transition.FadeInTransition

object WinScreen {
  val ID = 10
}

class WinScreen extends BasicGameState {

  var game: StateBasedGame = null

  override def getID = WinScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.getInput().isKeyPressed(Input.KEY_ENTER)) {
      game.enterState(MainMenuScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.stonewall.draw(0, 0)
    HUD.logo.draw(Config.Resolution.getX / 2 - HUD.logo.getWidth / 2, 40)
    Config.UIFont.drawString(350, 150, "You have completed this castle!")
    Config.castleSelectFont.drawString(370, 220, SharedStateData.winString)
    Config.castleSelectFont.drawString(600, Config.Resolution.getY - 50, "Press Enter")
  }
}