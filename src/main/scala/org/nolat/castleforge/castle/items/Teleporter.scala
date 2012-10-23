package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprites.value2string

class Teleporter(teleType: Int, _idcolor: String) extends Item with IDColor {
  //teleType: 0 = sender, 1 = receiver, 2 = bidirectional
  idcolor = IDColor.fromString(_idcolor)
  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(0), paramList(1))
  }

  sprite = new Sprite(getItemType)
  teleType match {
    case 0 => sprite.setAnimation("sender")
    case 1 => sprite.setAnimation("receiver")
    case 2 => sprite.setAnimation("bidirectional")
  }
  color = idcolor

  override def getItemType = Sprites.teleporter

  override def getParamList = {
    Seq(teleType.toString, idcolor.toString)
  }
}