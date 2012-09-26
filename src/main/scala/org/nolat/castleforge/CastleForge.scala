package org.nolat.castleforge

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.states.TitleScreen
import org.nolat.castleforge.states.MainMenuScreen

class CastleForge(title: String) extends StateBasedGame(title) {

  override def initStatesList(container: GameContainer) {
    addState(new TitleScreen())
    addState(new MainMenuScreen())
  }
}