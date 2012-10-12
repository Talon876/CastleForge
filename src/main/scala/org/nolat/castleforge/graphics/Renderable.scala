package org.nolat.castleforge.graphics

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

trait Renderable {

  def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

  }
}