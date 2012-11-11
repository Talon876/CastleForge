package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.ui.HUDElement
import org.newdawn.slick.gui.ComponentListener
import org.nolat.castleforge.ui.HUD
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.items.Item
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.Castle

class ElementToolSelector(castle: Castle, container: GameContainer) extends HUDElement(HUD.custom) with ComponentListener {

  var onToolChanged: (Tool) => Unit = { t => }

  lazy val itemTools = Item.itemTypes.zipWithIndex.map {
    case (iType, idx) =>
      val tool = new ItemTool(Item(iType).get,
        (position.x + ((idx % 5) * 64) + 6).toInt,
        (position.y + 64 + ((idx / 5) * 64) + 16).toInt,
        castle, container)
      tool.onClick = setCurrentTool
      tool
  }

  lazy val collectableItemTools = Item.collectables.zipWithIndex.map {
    case (iType, idx) =>
      val tool = new ItemTool(Item(iType).get,
        (position.x + (5 * 64 + 6) + ((idx % 2) * 64) + 6).toInt,
        (position.y + 64 + ((idx / 2) * 64) + 16).toInt,
        castle, container)
      tool.onClick = setCurrentTool
      tool
  }

  lazy val addRoomTool = {
    val tool = new AddRoomTool(
      (position.x + (2 * 64) + ((0 % 3) * 64) + 6).toInt - 32,
      (position.y).toInt,
      castle, container)
    tool.onClick = setCurrentTool
    tool
  }

  lazy val deleteRoomTool = {
    val tool = new DeleteRoomTool(
      (position.x + (2 * 64) + ((1 % 3) * 64) + 6).toInt,
      (position.y).toInt,
      castle, container)
    tool.onClick = setCurrentTool
    tool
  }

  lazy val eraserTool = {
    val tool = new EraserTool(
      (position.x + (2 * 64) + ((2 % 3) * 64) + 6).toInt + 32,
      (position.y).toInt,
      castle, container)
    tool.onClick = setCurrentTool
    tool
  }

  lazy val miscTools = List(addRoomTool, deleteRoomTool, eraserTool)

  var selected: Tool = null

  override def enter(container: GameContainer, game: StateBasedGame) {
    selected = addRoomTool
  }

  private def itemToolsHeight = {
    ((((itemTools.size - 1) / 5) + 1) * 64) + 10
  }

  private def collectableItemToolsHeight = {
    ((((collectableItemTools.size - 1) / 2) + 1) * 64) + 10
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setColor(new Color(255, 255, 255, 32))
    g.fillRoundRect(position.x, position.y + 64 + 6, 64 * 5 + 6, itemToolsHeight, 12)
    g.fillRoundRect(position.x + (5 * 64 + 12), position.y + 64 + 6, 64 * 2 + 12, collectableItemToolsHeight, 12)
    g.fillRoundRect(miscTools.head.x - 8, miscTools.head.y - 8, (miscTools.size + 1) * 64 + 8, 64 + 14, 12)

    itemTools.foreach(_.render(container, game, g))
    collectableItemTools.foreach(_.render(container, game, g))
    miscTools.foreach(_.render(container, game, g))

    HUD.selector.draw(selected.x, selected.y, Color.green)
  }

  private def setCurrentTool(tool: Tool) {
    selected = tool
    onToolChanged(selected)
  }

  override def componentActivated(source: AbstractComponent) {

  }
}