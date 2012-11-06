package org.nolat.castleforge.castle.items

import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite

class CrystalBall extends Collectable{
  sprite = new Sprite(getItemType)

  override def getItemType = Sprites.crystal_ball
  
  
  override def equalCollectable(that: Collectable): Boolean = {
    that match
    {
      case cb : CrystalBall => true
      case _ => false
    }
  }
  
}