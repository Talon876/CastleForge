package org.nolat.castleforge.states

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.newdawn.slick.state.transition.EmptyTransition

object CastleLoading {
  val ID = 5
}
class CastleLoading extends BasicGameState {

  var game: StateBasedGame = null

  override def getID = CastleLoading.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    SharedStateData.loadedCastle = (SharedStateData.mapFile, false)
    this.game.enterState(ExperimentScreen.ID, new EmptyTransition(), new EmptyTransition())
  }
  
  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.UIFont.drawString(600, 300, "Loading")
  }
}