package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.items.Wall

class ElementEditorHelp() extends HUDElement(HUD.custom) {

  var active = false

  def toggle() = active = !active

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (active) {
      g.setColor(new Color(60, 60, 60, .8f))
      g.fillRect(position.x, position.y, 600, 600)
      g.setColor(Color.black)
      //g.drawString("<space>", position.x + 4, position.y + 300)
      messages.zipWithIndex.foreach {
        case (message, idx) =>
          g.drawString(message, position.x + 4, position.y + idx * 24)
      }
    }

  }

  val messages = List("<F1> toggles this help screen",
    "* <Space> toggles the minimap",
    "* Use WASD to navigate around the map",
    "* The map grows when you approach the edge",
    "------------",
    "* Select a tool on the right and its options will appear below",
    "* Select the options by clicking on them",
    "* Note that some options won't have a visual",
    "  representation on the tool icon",
    "* To apply a tool to an area,",
    "  click and drag a selection on the map",
    "-----------",
    "* When placing rooms, make sure one wall",
    " overlaps the wall of an existing room")

}