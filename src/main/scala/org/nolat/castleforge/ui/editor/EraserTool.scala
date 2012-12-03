package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Wall
import org.nolat.castleforge.castle.items.Door
import org.nolat.castleforge.castle.items.Item

class EraserTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def apply(region: List[List[Floor]]) {
    //region.flatten.foreach { floor => CastleUtil.removeItem(castle, floor.getTilePosition) }
    filterOutWalls(region.flatten).foreach { floor =>
      floor.item match {
        case Some(itm) => itm match {
          case d: Door => { //when doors are deleted they become walls
            CastleUtil.removeItem(castle, floor.getTilePosition)
            CastleUtil.addItem(castle, floor.getTilePosition, Item("wall"))
          }
          case _ => CastleUtil.removeItem(castle, floor.getTilePosition)
        }
        case None => //nop
      }
    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Tool.eraser.draw(x, y)
    super.render(container, game, g)
  }

  private def filterOutWalls(floors: List[Floor]): List[Floor] = {
    floors.filter(tile => tile.item match {
      case Some(itm) => itm match {
        case w: Wall => false
        case _ => true
      }
      case None => true
    })
  }
}
