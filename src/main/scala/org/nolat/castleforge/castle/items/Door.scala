package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.items.attributes.Shape
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.graphics.Sprites

class Door(var doorType: Int, _idColor: String = "", _shape: String = "") extends Item with IDColor with Shape {
  //doorType: 0 = normal, 1 = locked, 2 = hidden
  idcolor = IDColor.fromString(_idColor)
  shape = Shape.fromString(_shape)

  def this(paramList: List[String]) = {
    this(paramList(0).intOption.getOrElse(0), if (paramList.length > 1) paramList(1) else "", if (paramList.length > 1) paramList(2) else "")
  }

  sprite = new Sprite(getItemType)

  doorType match {
    case 0 => sprite.setAnimation("unlocked")
    case 1 => sprite.setAnimation("locked")
    case 2 => sprite.setAnimation("unlocked")
  }

  val keySprite = new Sprite(Sprites.key)
  keySprite.setAnimation(shape)

  override def getItemType = Sprites.door

  override def getParamList = {
    Seq(doorType.toString, IDColor.toString(idcolor), shape)
  }

  override def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {

    sprite.getAnimation.draw(x, y, color)

    if (doorType == 1) {
      keySprite.getAnimation.draw(x + 26, y + 11, 16, 16, idcolor)
    }
  }
}