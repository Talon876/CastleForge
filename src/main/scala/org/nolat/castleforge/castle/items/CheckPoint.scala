package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.CheckPointState
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.xml.MapSave

class CheckPoint(isActive: Boolean) extends Item with CheckPointState {
  checkpointstate = CheckPointState.fromBoolean(isActive)

  def this(paramList: List[String]) = {
    this(paramList(0).booleanOption.getOrElse(false))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation(if (isActive) "active" else "inactive")

  override def getItemType = Sprites.checkpoint

  override def getParamList = {
    Seq(if (checkpointstate) "1" else "0")
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    if (checkpointstate == CheckPointState.INACTIVE) {
      CastleUtil.activateCheckPoint(player.castle, this)
    }
    MapSave.save(player.castle)
  }

  override def activate() {
    super.activate
    sprite.setAnimation("active")
  }

  override def deactivate() {
    super.deactivate
    sprite.setAnimation("inactive")
  }
}