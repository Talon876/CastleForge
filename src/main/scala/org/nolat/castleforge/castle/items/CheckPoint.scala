package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.CheckPointState
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.tools.Preamble._

class CheckPoint(isActive: Boolean) extends Item with CheckPointState {
  checkpointstate = CheckPointState.fromBoolean(isActive)

  def this(paramList: List[String]) = {
    this(paramList(0).booleanOption.getOrElse(false))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation(if (isActive) "active" else "inactive")

  override def getItemType = Sprites.checkpoint

  override def getParamList = {
    Seq(if (isActive) "1" else "0")
  }
}