package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Input
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.ui.editor.ElementToolSelector
import org.nolat.castleforge.ui.editor.ElementToolManager
import org.nolat.castleforge.ui.editor.ElementToolOptions
import org.nolat.castleforge.ui.editor.Tool
import org.newdawn.slick.state.StateBasedGame

class EditorHUD(castle: Castle, container: GameContainer, game: StateBasedGame) extends HUD {
  val borders = new HUDElement(HUD.border)
  this add borders

  val grooves = new HUDElement(HUD.grooves)
  grooves.position = new Vector2f(728, 8)
  this add grooves

  val logo = new HUDElement(HUD.logo)
  logo.position = new Vector2f(728, 588)
  this add logo

  val minimap = new ElementMiniMap(castle)
  minimap.position = new Vector2f(8 + 64 - 12, 8 + 64 - 12)
  this add minimap

  val floorSelector = new ElementFloorSelector(castle, container)
  floorSelector.position = new Vector2f(8, 8)
  this.container.getInput.addMouseListener(floorSelector)
  this add floorSelector

  val toolSelector = new ElementToolSelector(castle, container)
  toolSelector.position = new Vector2f(grooves.position.x + 16 + 32, grooves.position.y + 16)
  toolSelector.onToolChanged = toolChanged
  this add toolSelector

  val toolManager = new ElementToolManager(castle, toolSelector)
  toolManager.position = new Vector2f(8, 8)
  floorSelector.onToolReleased = toolManager.toolReleased
  this add toolManager

  val toolOptions = new ElementToolOptions()
  toolOptions.position = new Vector2f(toolSelector.position.x + 6, toolSelector.position.y + 64 * 3 + 32)
  toolOptions.onOptionsChanged = toolManager.optionsChanged
  this add toolOptions

  val pauseMenu = new ElementEditorMenu(castle, game)
  pauseMenu.position = new Vector2f(8, 8)
  this add pauseMenu

  def toggleMinimap() = minimap.toggle()

  def keyReleased(key: Int, c: Char) {
    key match {
      case Input.KEY_SPACE => toggleMinimap()
      case _ =>
    }

  }

  def toolChanged(newTool: Tool) {
    toolOptions.updateTool(newTool)
  }

}