package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprites.value2string
import org.newdawn.slick.Input
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.CastleUtil

class Teleporter(val teleType: String, _idcolor: String) extends Item with IDColor {
  idcolor = IDColor.fromString(_idcolor)
  def this(paramList: List[String]) = {
    this(paramList(0), paramList(1))
  }
  val speedMod = 5f

  sprite = new Sprite(getItemType)
  teleType match {
    case "sender" => sprite.setAnimation("sender")
    case "receiver" => sprite.setAnimation("receiver")
    case "bidirectional" => sprite.setAnimation("bidirectional")
  }
  color = idcolor

  override def getItemType = Sprites.teleporter

  override def getParamList = {
    Seq(teleType.toString, IDColor.toString(idcolor))
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    teleType match {
      case "sender" => senderEnter(player, srcFloor)
      case "receiver" =>
      case "bidirectional" => senderEnter(player, srcFloor)
    }

  }

  private def senderEnter(player: Player, srcFloor: Floor) {
    player.teleportMove(CastleUtil.findMatchingTeleporter(player.castle, this).getTilePosition, "teleporting", speedMod)
  }
}