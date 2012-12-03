package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Readable
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.states.SharedStateData
import org.nolat.castleforge.states.WinScreen
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.Color
import org.newdawn.slick.state.transition.FadeInTransition

class EndPoint(endText: String) extends Item with Readable {
  displayText = endText

  def this(paramList: List[String]) = {
    this(paramList(0))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation("default")

  override def getItemType = Sprites.endpoint

  override def getParamList = {
    Seq(displayText)
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    //println(displayText)
    SharedStateData.winString = displayText
    player.castle.game.enterState(WinScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))
  }

  override def getOptions = {
    List("text")
  }

  override def setOptions(options: List[Any]) {
    displayText = options(0).asInstanceOf[String]
  }
}