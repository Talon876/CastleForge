package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Renderable
import org.nolat.castleforge.graphics.Sprite
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.Castle

trait GameItem {
  var sprite: Sprite = null
  var position: Vector2f = new Vector2f(0, 0)
  var color: Color = Color.white

  def update(castle: Castle, container: GameContainer, game: StateBasedGame, delta: Int) {
  }

  def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    this.sprite.getAnimation.draw(x, y, this.color)
  }
}