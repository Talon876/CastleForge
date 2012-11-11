package org.nolat.castleforge.ui

import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Image
import org.newdawn.slick.Graphics

class HUDElement(var image: Image) extends Renderable {

  var parent: HUDElement = null
  var position: Vector2f = new Vector2f(0f, 0f)
  var offset: Vector2f = new Vector2f(0f, 0f)

  def enter(container: GameContainer, game: StateBasedGame) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    image.draw(position.x + offset.x, position.y + offset.y)
  }
}