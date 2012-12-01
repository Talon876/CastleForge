package org.nolat.castleforge.ui.editor

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.ui.HUDElement
import org.nolat.castleforge.castle.items.attributes._

class ElementToolOptions extends HUDElement(HUD.custom) {

  var onOptionsChanged: (List[Any]) => Unit = { opts => println("default options changed") }

  var optionPanes: Map[String, OptionPane] = null
  var options: List[String] = Nil

  override def enter(container: GameContainer, game: StateBasedGame) {
    optionPanes = Map(
      "color" -> new RadioOptionPane(Tool.colors, IDColor.values, container, this),
      "quantity" -> new RadioOptionPane(Tool.quantities, Quantity.values, container, this),
      "shape" -> new RadioOptionPane(Tool.shapes, Shape.values, container, this),
      "luminosity" -> new RadioOptionPane(Tool.lumens, Luminosity.values, container, this),
      "direction" -> new RadioOptionPane(Tool.directions, Direction.values, container, this),
      "torchstate" -> new RadioOptionPane(Tool.torchstates, TorchState.values, container, this),
      "teleportertype" -> new RadioOptionPane(Tool.teleporterTypes, List("sender", "receiver", "bidirectional"), container, this),
      "doortype" -> new RadioOptionPane(Tool.doorType, List(0, 1, 2), container, this),
      "text" -> new TextOptionPane(container, position.x.toInt, position.y.toInt + 4, this))
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    optionPanes.foreach {
      case (option, pane) => pane.update(container, game, delta)
    }
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

  //called when a new tool is selected
  def updateTool(newTool: Tool) {
    options = newTool.getOptions
    optionPanes.get("text").get.reset() //move the textfield out of the window every time a new tool is selected so it can't get focus when invisible

    options.zipWithIndex.foreach {
      case (option, idx) =>
        optionPanes.get(option) match {
          case Some(o) => {
            o match {
              case rop: RadioOptionPane => rop.updateMoas(position.x.toInt, (position.y + (idx * 64) + 4).toInt)
              case top: TextOptionPane => {
                println("showing textfield")
                top.textfield.setLocation(top.textfield.getX(), top.y + 20) //move the textfield to the correct location for tools that should have it
                top.textfield.setText("")
              }
              case _ => //nop
            }
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
            o match {
              case rop: RadioOptionPane => rop.getSelectedOption
              case _ => //nop
            }
          }
          case None => //nop
        }
    }
    optionList
  }
}