package org.nolat.castleforge

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.states.TitleScreen

class CastleForge(title: String) extends StateBasedGame(title) {

  override def initStatesList(container: GameContainer) {
    addState(new TitleScreen())
  }
}