package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Input
import org.newdawn.slick.GameContainer

class EditorHUD(castle: Castle, container: GameContainer) extends HUD {
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

  def toggleMinimap() = minimap.toggle()

  def keyReleased(key: Int, c: Char) {
    key match {
      case Input.KEY_SPACE => toggleMinimap()
      case _ =>
    }

  }
}