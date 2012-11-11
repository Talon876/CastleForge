package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Wall

class EraserTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def apply(region: List[List[Floor]]) {
    filterOutWalls(region.flatten).foreach { floor =>
      CastleUtil.removeItem(castle, floor.getTilePosition)
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
