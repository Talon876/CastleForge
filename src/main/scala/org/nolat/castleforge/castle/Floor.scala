package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import org.nolat.castleforge.graphics.Sprites

class Floor(val item: Option[Item], val x: Int, val y: Int) extends Renderable {
  var sprite = new Sprite(Sprites.floor)
  sprite.setRandomAnimation(List(0.33f, 0.33f, 0.33f, 0.1f))

  var translate: (Int, Int) => ((Int, Int)) = transform

  def canEnter(): Boolean = {
    true
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    item match {
      case Some(theItem) => theItem.update(container, game, delta)
      case None => //nothing
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    sprite.getAnimation.draw(x * Config.TileWidth + Config.TileOffsetX, y * Config.TileHeight + Config.TileOffsetY)
    item match {
      case Some(i) => i.render(container, game, g)
      case None =>
    }
  }

  private def transform(x: Int, y: Int): (Int, Int) = {
    (x, y)
  }
}