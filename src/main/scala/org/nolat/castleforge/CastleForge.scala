package org.nolat.castleforge

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.states._

class CastleForge(title: String) extends StateBasedGame(title) {

  override def initStatesList(container: GameContainer) {
    addState(new TitleScreen())
    addState(new MainMenuScreen())
    addState(new ExperimentScreen())
    addState(new ExperimentScreen2())
    addState(new CastleLoading())
    addState(new GameScreen())
    addState(new CreateCastleScreen())
    addState(new SaveScreen())
    addState(new CastleSelectScreen())
  }
}