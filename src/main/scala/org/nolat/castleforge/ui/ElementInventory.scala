package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Player
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class ElementInventory(player: Player) extends HUDElement(HUD.custom) {

  val key = new Sprite(Sprites.key)

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    g.setColor(new Color(255, 255, 255, 32))
    val keyAmount = player.inventory.getKeys.size
    if (keyAmount == 0) {
      g.fillRoundRect(position.x, position.y, 64 * 8, 22, 12)
    } else {
      println(keyAmount / 8)
      g.fillRoundRect(position.x, position.y, 64 * 8, ((((keyAmount - 1) / 8) + 1) * 64) + 6, 12)
    }
    g.setColor(Color.darkGray)

    g.setColor(Color.black)
    g.drawString("~Keys~", position.x + 6, position.y + 0)
    player.inventory.getKeys.zipWithIndex.foreach {
      case (key, idx) =>
        key.sprite.getAnimation.draw(position.x + ((idx % 8) * 64) + 6, position.y + ((idx / 8) * 64) + 10, key.idcolor)
    }

  }

}