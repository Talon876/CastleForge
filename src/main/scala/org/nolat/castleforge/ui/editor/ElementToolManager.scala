package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.ui.HUDElement
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.Castle

class ElementToolManager(castle: Castle, toolSelector: ElementToolSelector) extends HUDElement(HUD.custom) {

  def currentTool = toolSelector.selected

  def toolReleased(region: List[List[(Int, Int)]]) {
    println("tool released")
    val floorRegion = region.map { row =>
      row.map { coord =>
        CastleUtil.floorAt(castle, coord)
      }
    }
    currentTool.apply(floorRegion)
  }

  def optionsChanged(options: List[Any]) {
    currentTool.setOptions(options)
  }

}