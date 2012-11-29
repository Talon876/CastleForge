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
          case d: Door => {
            if (floor.roomIDlist.size == 2) { //only check neighbors to place door if it's a 2-room intersection

              val left = CastleUtil.floorAt(castle, (floor.getTilePosition._1 - 1, floor.getTilePosition._2))
              val right = CastleUtil.floorAt(castle, (floor.getTilePosition._1 + 1, floor.getTilePosition._2))
              val up = CastleUtil.floorAt(castle, (floor.getTilePosition._1, floor.getTilePosition._2 - 1))
              val down = CastleUtil.floorAt(castle, (floor.getTilePosition._1, floor.getTilePosition._2 + 1))
              //left neighbor and right neighbor have to belong to only 1 roomid
              //or top neighbor and bottom neighbor have to belong to only 1 room id
              val leftRight = left.roomIDlist.size == 1 && right.roomIDlist.size == 1
              val topBottom = up.roomIDlist.size == 1 && down.roomIDlist.size == 1
              if (leftRight || topBottom) {
                CastleUtil.addItem(castle, floor.getTilePosition, Item(item.getItemType, item.getParamList.toList))
              }
            }
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