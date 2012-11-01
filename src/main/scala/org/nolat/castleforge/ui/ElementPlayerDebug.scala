package org.nolat.castleforge.ui

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.castle.Player
import org.newdawn.slick.Color

class ElementPlayerDebug(player: Player) extends HUDElement(HUD.custom) {

  var active = true

  def toggle() = active = !active

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (active) {
      g.setColor(new Color(255, 255, 255, 128))
      g.fillRoundRect(position.x, position.y, 64f * 4f, 64f * 2f, 12)
      g.setColor(Color.black)

      messages.zipWithIndex.foreach {
        case (msg, indx) => g.drawString(msg, position.x + 3, position.y + 2 + 15 * indx)
      }
    }
  }

  def messages = {
    List("Tile: " + player.tilePosition.toString,
      "State: " + player.state,
      "Animation: " + player.sprite,
      "Inv. Size: " + player.inventory.size)
  }
}