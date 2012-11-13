package org.nolat.castleforge.ui.editor

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.ui.HUDElement
import org.nolat.castleforge.castle.items.attributes._
import org.newdawn.slick.Color

class ElementToolOptions extends HUDElement(HUD.custom) {

  var onOptionsChanged: (List[Any]) => Unit = { opts => println("default options changed") }

  var optionPanes: Map[String, OptionPane] = null
  var options: List[String] = Nil

  override def enter(container: GameContainer, game: StateBasedGame) {
    optionPanes = Map(
      "color" -> new OptionPane(Tool.colors, IDColor.values, container, this),
      "quantity" -> new OptionPane(Tool.quantities, Quantity.values, container, this),
      "shape" -> new OptionPane(Tool.shapes, Shape.values, container, this),
      "luminosity" -> new OptionPane(Tool.lumens, Luminosity.values, container, this),
      "direction" -> new OptionPane(Tool.directions, Direction.values, container, this),
      "torchstate" -> new OptionPane(Tool.torchstates, TorchState.values, container, this),
      "teleportertype" -> new OptionPane(Tool.teleporterTypes, List("sender", "receiver", "bidirectional"), container, this),
      "doortype" -> new OptionPane(Tool.doorType, List(0, 1, 2), container, this))
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {

    g.setColor(new Color(255, 255, 255, 32))
    if (numOptions > 0) g.fillRoundRect((position.x).toInt, (position.y).toInt, 64 * 7 + 6, options.size * 64 + 8, 12)

    options.zipWithIndex.foreach {
      case (option, idx) =>
        optionPanes.get(option) match {
          case Some(o) => o.render((position.x).toInt, (position.y + (idx * 64) + 4).toInt, container, game, g)
          case None => //nop
        }
    }
  }

  private def numOptions = {
    (optionPanes.map {
      case (option, pane) => option
    }.toList intersect options).size
  }

  def updateTool(newTool: Tool) {
    println("tool updated")
    options = newTool.getOptions

    options.zipWithIndex.foreach {
      case (option, idx) =>
        optionPanes.get(option) match {
          case Some(o) => {
            o.updateMoas(position.x.toInt, (position.y + (idx * 64) + 4).toInt)
            //o.selected = 0 //set each option to first as default
            //o.selectedCoords = (position.x.toInt, (position.y + (idx * 64) + 4).toInt)
          }
          case None => //nop
        }
    }
  }

  def getSelectedOptions = {
    val optionList = options.zipWithIndex.map {
      case (option, idx) =>
        optionPanes.get(option) match {
          case Some(o) => {
            o.getSelectedOption
          }
          case None => //nop
        }
    }
    optionList
  }
}