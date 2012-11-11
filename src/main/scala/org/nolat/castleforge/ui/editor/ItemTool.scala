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

class ItemTool(var item: Item, x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    item.render(x, y, container, game, g)
    super.render(container, game, g)
  }

  override def apply(region: List[List[Floor]]) {
    region.flatten.foreach { floor =>
      CastleUtil.addItem(castle, floor.getTilePosition, Some(item))
    }
  }
}