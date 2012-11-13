package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.graphics.Renderable
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Image
import org.newdawn.slick.gui.MouseOverArea
import org.nolat.castleforge.ui.HUD
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.Color

class OptionPane(optionImage: Image, options: List[Any], container: GameContainer, parent: ElementToolOptions) extends ComponentListener {
  var moas = {
    options.zipWithIndex.map {
      case (option, idx) =>
        val moa = new MouseOverArea(container, HUD.custom, (parent.position.x + (idx * 64)).toInt, 0, 64, 64, this)
        moa.setMouseOverImage(HUD.selector)
        moa
    }
  }

  var selected = 0
  var selectedCoords = (0, 0)

  def update(container: GameContainer, game: StateBasedGame, delta: Int) {

  }

  def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    optionImage.draw(x, y)
    moas.foreach(_.render(container, g))
    if (selected >= 0) {
      //HUD.selector.draw(selectedCoords._1, selectedCoords._2, Color.green)
    }
  }

  override def componentActivated(source: AbstractComponent) {
    val moa = source.asInstanceOf[MouseOverArea]
    println(options((moa.getX - parent.position.x.toInt) / 64))
    selected = (moa.getX - parent.position.x.toInt) / 64
    selectedCoords = (moa.getX, moa.getY)
    parent.onOptionsChanged(parent.getSelectedOptions)
  }

  def updateMoas(x: Int, y: Int) {
    moas.foreach {
      moa =>
        moa.setLocation(x + moas.indexOf(moa) * 64, y)
    }
  }

  def getSelectedOption = if (selected >= 0) options(selected)
}