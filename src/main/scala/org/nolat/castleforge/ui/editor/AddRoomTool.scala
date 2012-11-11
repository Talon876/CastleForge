package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.CastleUtil

class AddRoomTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def apply(region: List[List[Floor]]) {
    //    val coords = region.flatten.map(_.getTilePosition)
    //    CastleUtil.floorsAt(castle, CastleUtil.getPerimeter(coords)).foreach {
    //      floor =>
    //        CastleUtil.addItem(castle, floor.getTilePosition, Item("wall"))
    //    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Tool.addroom.draw(x, y)
    super.render(container, game, g)
  }
}