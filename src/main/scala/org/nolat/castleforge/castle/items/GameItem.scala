package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Renderable
import org.nolat.castleforge.graphics.Sprite
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Color

trait GameItem extends Renderable {
  var sprite: Sprite = null
  var position: Vector2f = new Vector2f(0, 0)
  var color: Color = Color.white

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(position.x, position.y, color)
  }
}