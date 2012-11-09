package org.nolat.castleforge.castle.items

import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.tools.Preamble._
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.castle.items.attributes.Shape
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.attributes.Collectable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color

class Key(_color: String, _shape: String, _quantity: Int) extends Item with IDColor with Quantity with Shape with Collectable {
  idcolor = IDColor.fromString(_color)
  shape = Shape.fromString(_shape)
  quantity = Quantity.fromInt(_quantity)

  def this(paramList: List[String]) = {
    this(paramList(0), paramList(1), paramList(2).intOption.getOrElse(1))
  }

  sprite = new Sprite(getItemType)
  sprite.setAnimation(shape)
  color = idcolor

  override def getItemType = Sprites.key

  override def getParamList = {
    Seq(IDColor.toString(idcolor), shape, quantity.toString)
  }

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    player.inventory.addItem(this)
    CastleUtil.removeItem(player.castle, player.tilePosition) //remove item at players position (this)
  }

  override def isSimilar(that: Collectable): Boolean = {
    that match {
      case key: Key => this.idcolor == key.idcolor && this.shape == key.shape
      case _ => false
    }
  }

  override def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    this.sprite.getAnimation.draw(x, y, this.color)
    g.setColor(Color.yellow)
    if (quantity > 1) g.drawString("x" + quantity, x + 5, y + 40)
  }
}