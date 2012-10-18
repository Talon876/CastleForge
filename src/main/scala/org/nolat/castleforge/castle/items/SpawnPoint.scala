package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.CheckPointState
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class SpawnPoint(isActive: Boolean) extends Item with CheckPointState {
  checkpointstate = CheckPointState.fromBoolean(isActive)

  def this(paramList: List[String]) = {
    this(paramList(0).booleanOption.getOrElse(false))
  }
  sprite = new Sprite(getItemType)
  sprite.setAnimation("default")

  override def getItemType = Sprites.spawnpoint

  override def getParamList = {
    Seq(checkpointstate.toString)
  }
}