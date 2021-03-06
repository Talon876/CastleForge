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
import org.nolat.castleforge.ui.editor.TextOptionPane
import org.nolat.castleforge.Config

class EditorHUD(castle: Castle, container: GameContainer, game: StateBasedGame) extends HUD {

  val grooves = new HUDElement(HUD.grooves)
  grooves.position = new Vector2f(728, 8)
  this add grooves

  val logoBG = new HUDElement(HUD.grooves)
  logoBG.position = new Vector2f(728, Config.Resolution.getY - HUD.grooves.getHeight)
  this add logoBG

  val logo = new HUDElement(HUD.logo)
  logo.position = new Vector2f(728, 588)
  this add logo

  val borders = new HUDElement(HUD.border)
  this add borders

  val minimap = new ElementMiniMap(castle)
  minimap.position = new Vector2f(8 + 64 - 12, 8 + 64 - 12)
  this add minimap

  val floorSelector = new ElementFloorSelector(castle, container, game)
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
  toolOptions.onOptionsChanged = toolManager.optionsChanged //called when the optionmenu moa is clicked
  this add toolOptions

  val pauseMenu = new ElementEditorMenu(castle, game)
  pauseMenu.position = new Vector2f(8, 8)
  this add pauseMenu

  val helpMenu = new ElementEditorHelp()
  helpMenu.position = new Vector2f(8 + 64 - 12, 8 + 64 - 12)
  this add helpMenu

  def toggleMinimap() = minimap.toggle()

  def toggleHelp() = helpMenu.toggle()

  def keyReleased(key: Int, c: Char) {
    if (container.isPaused) {
      minimap.active = false
      helpMenu.active = false
    } else {
      if (!TextOptionPane.textfieldInFocus) { //don't allow toggling if a textfield is in focus
        key match {
          case Input.KEY_SPACE => toggleMinimap()
          case Input.KEY_F1 => toggleHelp()
          case _ =>
        }
      }
    }
  }

  def toolChanged(newTool: Tool) {
    //println("EditorHUD:toolChanged: " + newTool.getOptions)
    toolOptions.updateTool(newTool)
  }

}