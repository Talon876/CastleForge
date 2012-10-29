package org.nolat.castleforge.castle.items

import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class Obstacle(oType: String) extends Item {

  def this(paramList: List[String]) = {
    this(paramList(0))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation(oType)
  isBlockingMovement = true

  override def getItemType = Sprites.obstacle

  override def getParamList = {
    Seq(oType)
  }
}