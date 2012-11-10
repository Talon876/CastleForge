package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.gui.MouseOverArea
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.CastleUtil
import org.newdawn.slick.MouseListener
import org.newdawn.slick.Input

class ElementFloorSelector(castle: Castle, container: GameContainer) extends HUDElement(HUD.custom) with ComponentListener with MouseListener {
  lazy val areas = (0 to 10).map { col =>
    (0 to 10).map { row =>
      //(row * 64, col * 64, 0)
      val moa = new MouseOverArea(container, HUD.custom, position.x.toInt + row * 64, position.y.toInt + col * 64, 64, 64, this)
      moa.setMouseOverColor(Color.green)
      moa.setMouseOverImage(HUD.selector)
      moa
    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    areas.flatten.foreach(_.render(container, g))
  }

  private def getMOA() = {

  }

  override def componentActivated(source: AbstractComponent) {
    val moa = source.asInstanceOf[MouseOverArea]
    val relativeCoords = ((moa.getX - position.x.toInt) / 64 - 5, (moa.getY - position.y.toInt) / 64 - 5)
    val absCoords = (relativeCoords._1 + castle.player.tilePosition._1, relativeCoords._2 + castle.player.tilePosition._2)
    println(relativeCoords + " " + absCoords + " " + CastleUtil.floorAt(castle, absCoords).itemName)
  }

  override def mouseClicked(button: Int, x: Int, y: Int, clickCount: Int) {
    println("clicked: " + x + " " + y)
  }

  override def mouseDragged(oldx: Int, oldy: Int, newx: Int, newy: Int) {
    //println("dragged: " + oldx + " " + newy + " to " + newx + " " + newy)
  }

  override def mouseMoved(oldx: Int, oldy: Int, newx: Int, newy: Int) {
    //println("moved: " + oldx + " " + newy + " to " + newx + " " + newy)
  }

  override def mousePressed(button: Int, x: Int, y: Int) {
    println("pressed: " + x + " " + y)
  }

  override def mouseReleased(button: Int, x: Int, y: Int) {
    println("released: " + x + " " + y)
  }

  override def mouseWheelMoved(change: Int) {}
  override def setInput(input: Input) {}
  override def inputStarted() {}
  override def inputEnded() {}
  override def isAcceptingInput() = true
}