package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Player
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Key

class ElementInventory(player: Player) extends HUDElement(HUD.custom) {

  val key = new Sprite(Sprites.key)

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    drawKeySection(g)
    drawMiscSection(g)
  }

  private def drawKeySection(g: Graphics) {
    g.setColor(new Color(255, 255, 255, 32))
    g.fillRoundRect(position.x, position.y, 64 * 8, keySectionHeight, 12)

    g.setColor(Color.black)
    g.drawString("~Keys~", position.x + 6, position.y + 0)
    player.inventory.getKeys.zipWithIndex.foreach {
      case (key, idx) =>
        key.sprite.getAnimation.draw(position.x + ((idx % 8) * 64) + 6, position.y + ((idx / 8) * 64) + 10, key.idcolor)
    }
  }

  private def keySectionHeight = {
    val keyAmount = player.inventory.getKeys.size
    if (keyAmount == 0) 22 else ((((keyAmount - 1) / 8) + 1) * 64) + 6
  }

  private def miscSectionHeight = {
    val miscAmount = miscInventory.size
    if (miscAmount == 0) 22 else ((((miscAmount - 1) / 8) + 1) * 64) + 6
  }

  private def drawMiscSection(g: Graphics) {
    g.setColor(new Color(255, 255, 255, 32))
    g.fillRoundRect(position.x, position.y + keySectionHeight + 8, 64 * 8, miscSectionHeight, 12)

    g.setColor(Color.black)
    g.drawString("~Misc~", position.x + 6, position.y + keySectionHeight + 8)

    miscInventory.zipWithIndex.foreach {
      case (itm, idx) =>
        itm.sprite.getAnimation.draw(position.x + ((idx % 8) * 64) + 6, position.y + ((idx / 8) * 64) + 10 + keySectionHeight + 8, itm.color)
    }
  }

  //this and isNotKey should be replaced by the inventory's implementation
  private def miscInventory = player.inventory.filter(isNotKey(_)).map(item => item match { case Some(x) => x }).toList

  private def isNotKey(item: Option[Item]) = {
    item match {
      case Some(x) => x match {
        case k: Key => false
        case _ => true
      }
      case None => false
    }
  }

}