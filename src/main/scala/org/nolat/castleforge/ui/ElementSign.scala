package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Player
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items._

class ElementSign(player: Player) extends HUDElement(HUD.sign) {

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (onReadable) {
      super.render(container, game, g)
      g.setColor(new Color(255, 255, 255, 128))
      //g.fillRoundRect(position.x, position.y, 64f * 9f, 64f * 7.5f, 12)
      g.setColor(Color.black)
      g.drawString(message, position.x + 12, position.y + 91)
    }
  }

  def onReadable = {
    CastleUtil.itemAt(player.castle, player.tilePosition) match {
      case Some(itm) => itm.isInstanceOf[Sign]
      case None => false
    }
  }

  def message = {
    if (onReadable) CastleUtil.itemAt(player.castle, player.tilePosition).get.asInstanceOf[Sign].displayText else ""
  }

}