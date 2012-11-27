package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.gui.MouseOverArea
import org.nolat.castleforge.ui.HUD
import org.newdawn.slick.gui.AbstractComponent
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Door

class ItemTool(var item: Item, x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    item.render(x, y, container, game, g)
    super.render(container, game, g)
  }

  override def apply(region: List[List[Floor]]) {
    region.flatten.foreach { floor =>
      if (!floor.roomIDlist.contains(0)) { //don't put items in floors with id = 0
        item match {
          case d: Door => { //only place door at a 2-room intersection
            if (floor.roomIDlist.size == 2) CastleUtil.addItem(castle, floor.getTilePosition, Item(item.getItemType, item.getParamList.toList))
          }
          case _ => CastleUtil.addItem(castle, floor.getTilePosition, Item(item.getItemType, item.getParamList.toList))
        }
      }
    }
  }

  override def getOptions() = item.getOptions

  override def setOptions(options: List[Any]) = {
    println("ItemTool: options: " + options)
    item.setOptions(options)
  }
}