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
import org.newdawn.slick.Color

class Teleporter(var teleType: String, _idcolor: String) extends Item with IDColor {
  idcolor = IDColor.fromString(_idcolor)
  def this(paramList: List[String]) = {
    this(paramList(0), paramList(1))
  }
  val speedMod = 5f

  sprite = new Sprite(getItemType)
  makeSpriteMatchOptions()

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

  override def getOptions = List("color", "teleportertype")

  override def setOptions(options: List[Any]) {
    idcolor = options(0).asInstanceOf[Color]
    teleType = options(1).asInstanceOf[String]
    makeSpriteMatchOptions()
  }

  def makeSpriteMatchOptions() {
    teleType match {
      case "sender" => sprite.setAnimation("sender")
      case "receiver" => sprite.setAnimation("receiver")
      case "bidirectional" => sprite.setAnimation("bidirectional")
    }
    color = idcolor
  }
}