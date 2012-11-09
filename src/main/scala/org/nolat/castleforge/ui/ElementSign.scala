package org.nolat.castleforge.ui

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.PlayerState
import org.nolat.castleforge.castle.items.Sign
import org.nolat.castleforge.tools.Lerper

class ElementSign(player: Player) extends HUDElement(HUD.sign) {

  object SignState extends Enumeration {
    type SignState = Value
    val HIDDEN, SLIDING_UP, SHOWING, SLIDING_DOWN = Value
  }

  private var y = 720f
  private var positionLerper = new Lerper(700)
  positionLerper.finishedLerp = stopSliding

  private var state = SignState.HIDDEN //hidden = 0, sliding up = 1, showing = 2, sliding down = 3
  private var lastMsg = ""

  private def stopSliding() = {
    if (state == SignState.SLIDING_UP) {
      state = SignState.SHOWING
    } else if (state == SignState.SLIDING_DOWN) {
      state = SignState.HIDDEN
    }

  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (state == SignState.HIDDEN && onReadable) {
      state = SignState.SLIDING_UP
      positionLerper.start(y, 100)
    }

    if (state == SignState.SHOWING && !onReadable) {
      state = SignState.SLIDING_DOWN
      positionLerper.start(y, 720)
    }

    if (state == SignState.SLIDING_UP || state == SignState.SLIDING_DOWN) {
      y = positionLerper.value
    }

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (state != 0) {
      image.draw(position.x + offset.x, y + offset.y, new Color(255, 255, 255, .8f))
      g.setColor(Color.black)
      g.drawString(message, position.x + 12, y + 91)
    }
  }

  def onReadable = {
    CastleUtil.itemAt(player.castle, player.tilePosition) match {
      case Some(itm) => itm.isInstanceOf[Sign] && player.state == PlayerState.IDLE
      case None => false
    }
  }

  def message = {
    if (onReadable) {
      lastMsg = CastleUtil.itemAt(player.castle, player.tilePosition).get.asInstanceOf[Sign].displayText
      lastMsg
    } else {
      lastMsg
    }
  }

}