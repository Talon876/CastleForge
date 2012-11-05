package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.castle.items.attributes.Luminosity
import org.nolat.castleforge.castle.items.attributes.TorchState

class Torch(lit: Boolean, lumen: String, _color: String) extends Item with IDColor with Luminosity with TorchState {
  torchstate = TorchState.fromBoolean(lit)
  luminosity = Luminosity.fromString(lumen)
  idcolor = IDColor.fromString(_color)

  def this(paramList: List[String]) = {
    this(paramList(0).booleanOption.getOrElse(true), paramList(1), paramList(2))
  }

  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.torch

  override def getParamList = {
    Seq(torchstate.toString, luminosity.toString, IDColor.toString(idcolor))
  }
}