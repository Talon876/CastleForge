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
import org.nolat.castleforge.tools.MoveDescription
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.states.CreateCastleScreen

class ElementFloorSelector(castle: Castle, container: GameContainer, game: StateBasedGame) extends HUDElement(HUD.custom) with ComponentListener with MouseListener {
  lazy val areas = (0 to 10).map { col =>
    (0 to 10).map { row =>
      //(row * 64, col * 64, 0)
      val moa = new MouseOverArea(container, HUD.custom, position.x.toInt + row * 64, position.y.toInt + col * 64, 64, 64, this)
      moa.setMouseOverColor(Color.green)
      moa.setMouseOverImage(HUD.selector)
      moa
    }
  }

  var first = (0, 0)
  var second = (0, 0)
  var selection: List[List[(Int, Int)]] = Nil
  var mouseDown = false

  var onToolReleased: (List[List[(Int, Int)]]) => Unit = { t => }

  override def enter(container: GameContainer, game: StateBasedGame) {
    castle.player.moved = playerMoved
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    areas.flatten.foreach(_.render(container, g))
    renderSelection(g, absoluteToLocal(selection.flatten))

    if (selection != Nil) {
      g.setColor(new Color(255, 255, 255, 32))
      g.fillRoundRect(8, 8, 42, 18, 6)
      g.setColor(Color.black)
      g.drawString(selection.size + "x" + selection(0).size, 10, 10)
    }

  }

  private def renderSelection(g: Graphics, selection: List[(Int, Int)]) = {
    selection.foreach { coord =>
      g.setColor(new Color(0, 200, 0, .2f))
      g.fillRect(position.x + coord._1 * 64, position.y + coord._2 * 64, 64, 64)
    }
  }

  private def absoluteToLocal(coords: List[(Int, Int)]) = {
    coords.map { coord =>
      (coord._1 - castle.player.tilePosition._1 + 5, coord._2 - castle.player.tilePosition._2 + 5)
    }.filter { coord =>
      coord._1 >= 0 && coord._1 <= 10 && coord._2 >= 0 && coord._2 <= 10
    }
  }

  private def getMOA() = {
    val candidates = areas.flatten.filter(_.isMouseOver)
    if (candidates.size > 0) Some(candidates(0)) else None
  }

  private def getAbsCoords(moa: MouseOverArea) = {
    val relativeCoords = ((moa.getX - position.x.toInt) / 64 - 5, (moa.getY - position.y.toInt) / 64 - 5)
    val absCoords = (relativeCoords._1 + castle.player.tilePosition._1, relativeCoords._2 + castle.player.tilePosition._2)
    absCoords
  }

  override def componentActivated(source: AbstractComponent) {
  }

  override def mouseClicked(button: Int, x: Int, y: Int, clickCount: Int) {

  }

  override def mouseDragged(oldx: Int, oldy: Int, newx: Int, newy: Int) {
    getMOA match {
      case Some(moa) => {
        second = getAbsCoords(moa)
        selection = CastleUtil.getSelectedCoordinates(castle, first, second)
      }
      case None => //nop
    }
  }

  private def playerMoved(md: MoveDescription) {
    if (mouseDown) {
      getMOA match {
        case Some(moa) => {
          second = getAbsCoords(moa)
          selection = CastleUtil.getSelectedCoordinates(castle, first, second)
        }
        case None => //nop
      }
    }
  }

  override def mouseMoved(oldx: Int, oldy: Int, newx: Int, newy: Int) {}

  override def mousePressed(button: Int, x: Int, y: Int) {
    mouseDown = true
    getMOA match {
      case Some(moa) => {
        first = getAbsCoords(moa)
      }
      case None => //nop
    }
  }

  override def mouseReleased(button: Int, x: Int, y: Int) {
    if (game.getCurrentStateID == CreateCastleScreen.ID) {
      mouseDown = false
      getMOA match {
        case Some(moa) => {
          second = getAbsCoords(moa)
          selection = CastleUtil.getSelectedCoordinates(castle, first, second)
          onToolReleased(selection)
          selection = Nil
        }
        case None => {
          selection = Nil
        }
      }
    }
  }

  override def mouseWheelMoved(change: Int) {}
  override def setInput(input: Input) {}
  override def inputStarted() {}
  override def inputEnded() {}
  override def isAcceptingInput() = true
}