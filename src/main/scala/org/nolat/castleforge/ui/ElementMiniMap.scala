package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.items.Wall

class ElementMiniMap(castle: Castle) extends HUDElement(HUD.custom) {

  var active = false

  def toggle() = active = !active

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (active) {
      drawCastle(g)
      g.setColor(new Color(60, 60, 60, .2f))
      g.fillRect(position.x, position.y, 600, 600)
      g.setColor(Color.black)
      //g.drawString("<space>", position.x + 4, position.y + 300)
    }

  }

  private def drawCastle(g: Graphics) {
    g.setColor(Color.black)
    g.drawRect(position.x, position.y, 600, 600)
    castle.map.flatten.foreach { floor =>
      val x = floor.getTilePosition._1
      val y = floor.getTilePosition._2
      floor.item match {
        case Some(itm) => itm match {
          case w: Wall => g.setColor(Color.black); g.drawRect(position.x + x * 2, position.y + y * 2, 1, 1)
          case _ => g.setColor(Color.red); g.drawRect(position.x + x * 2, position.y + y * 2, 1, 1)
        }
        case None =>
      }
    }
    g.setColor(Color.white)
    val px = position.x + castle.player.tilePosition._1 * 2
    val py = position.y + castle.player.tilePosition._2 * 2
    g.drawRect(px, py, 1, 1)
    g.drawRect(px - 11, py - 11, 23, 23)
  }

}