package org.nolat.castleforge.ui

import scala.collection.mutable.MutableList
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.newdawn.slick.Input
import org.nolat.castleforge.graphics.Renderable

class Menu(var position: Vector2f) extends Renderable {
  def this() = this(new Vector2f(0, 0))

  val menuItems = MutableList[MenuItem]()

  var colorSelected = Color.white
  var colorNotselected = Color.darkGray

  private var selectedIndex = 0

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {

    menuItems(selectedIndex).isSelected = true
    menuItems.foreach(_.update(container, game, delta))

    if (container.getInput().isKeyPressed(Input.KEY_DOWN) ||
      container.getInput().isKeyPressed(Input.KEY_S)) {
      menuItems(selectedIndex).isSelected = false
      selectedIndex += 1
      selectedIndex %= menuItems.size
    } else if (container.getInput().isKeyPressed(Input.KEY_UP) ||
      container.getInput().isKeyPressed(Input.KEY_W)) {
      menuItems(selectedIndex).isSelected = false
      selectedIndex -= 1
      if (selectedIndex < 0) { selectedIndex = menuItems.size - 1 }
    }

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    menuItems.foreach { item =>
      val yOffset = menuItems.indexOf(item) * Config.UIFont.getHeight(item.text) + 8

      item.position = new Vector2f(position.x, position.y + yOffset)
      item.render(container, game, g)
    }
  }

  /**
   * Add a menu item with default action
   * @param name the name of the menu item
   */
  def add(name: String) = {
    menuItems += new MenuItem(name)
  }

  /**
   * Add a menu item with custom action
   * @param name the name of the menu item
   * @param onClick the method to run when clicked
   */
  def add(name: String, onClick: (String, Vector2f) => Unit) {
    menuItems += new MenuItem(name, onClick)
  }

  class MenuItem(val text: String, val onClick: (String, Vector2f) => Unit) extends Renderable {
    def this(text: String) = this(text, (txt, position) => println(txt + " was clicked, but is still using the default event."))

    var isSelected = false
    var position = new Vector2f(0, 0)

    override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
      if (isSelected) {
        if (container.getInput().isKeyPressed(Input.KEY_ENTER) ||
          container.getInput().isKeyPressed(Input.KEY_SPACE)) {
          onClick(text, position)
        }
      }
    }

    override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
      Config.UIFont.drawString(position.x, position.y, text, if (isSelected) colorSelected else colorNotselected)
    }
  }
}

