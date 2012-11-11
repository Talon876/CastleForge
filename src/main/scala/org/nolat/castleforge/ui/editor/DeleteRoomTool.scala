package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.Castle

class DeleteRoomTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def apply(region: List[List[Floor]]) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Tool.deleteroom.draw(x, y)
    super.render(container, game, g)
  }
}