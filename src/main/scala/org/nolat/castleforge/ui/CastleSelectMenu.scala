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
import java.io.File
import org.nolat.castleforge.xml.MapUtils

class CastleSelectMenu(var position: Vector2f) extends Renderable {
  def this() = this(new Vector2f(0, 0))

  val menuItems = MutableList[CastleSelectMenuItem]()

  var colorSelected = Color.white
  var colorNotselected = Color.darkGray.darker.darker
  var charactersPerLine: Int = 100
  var itemsDisplayed: Int = 4
  private var selectedIndex = 0
  private var topIdx: Int = math.max(0, selectedIndex - (itemsDisplayed / 2))
  private var bottomIdx: Int = math.min(menuItems.size - 1, selectedIndex + (itemsDisplayed / 2))

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (menuItems.length > 0) {
      menuItems(selectedIndex).isSelected = true
      menuItems.foreach(_.update(container, game, delta))

      if (container.getInput().isKeyPressed(Input.KEY_DOWN) ||
        container.getInput().isKeyPressed(Input.KEY_S)) {
        menuItems(selectedIndex).isSelected = false
        selectedIndex += 1
        selectedIndex %= menuItems.size
        setIndecies()

      } else if (container.getInput().isKeyPressed(Input.KEY_UP) ||
        container.getInput().isKeyPressed(Input.KEY_W)) {
        menuItems(selectedIndex).isSelected = false
        selectedIndex -= 1
        if (selectedIndex < 0) { selectedIndex = menuItems.size - 1 }
        setIndecies()
      }
    }

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (menuItems.length > 0) {
      var yOffset = 0
      for (i <- topIdx to bottomIdx) {
        val item: CastleSelectMenuItem = menuItems.get(i).get
        item.position = new Vector2f(position.x, position.y + yOffset)
        item.render(container, game, g)
        yOffset = yOffset + item.height + 40
      }
    } else {
      Config.castleSelectFont.drawString(position.x, position.y, "No .map files found in " + Config.WorkingDirectory + "/maps", colorNotselected)
    }
  }

  def setIndecies() {
    topIdx = math.max(0, selectedIndex - (itemsDisplayed / 2))
    if (topIdx == 0 && selectedIndex == 0) {
      bottomIdx = math.min(menuItems.size - 1, itemsDisplayed - 1);
    } else {
      bottomIdx = math.min(menuItems.size - 1, selectedIndex + (itemsDisplayed / 2) + ((selectedIndex - topIdx) % (itemsDisplayed / 2)))
      //selectedIndex + (itemsDisplayed / 2) puts half of the items below the current selected one
      //((selectedIndex - topIdx) % (itemsDisplayed / 2)) if there are less than half of the items above the current selected one it will add the remainder to the bottom
    }
  }
  /**
   * Add a menu item with default action
   * @param name the name of the menu item
   */
  def add(file: File) = {
    menuItems += new CastleSelectMenuItem(file)
    setIndecies()
  }

  /**
   * Add a menu item with custom action
   * @param name the name of the menu item
   * @param onClick the method to run when clicked
   */
  def add(file: File, onClick: (File, Int) => Unit) {
    menuItems += new CastleSelectMenuItem(file, onClick)
    setIndecies()
  }

  class CastleSelectMenuItem(val file: File, val onClick: (File, Int) => Unit) extends Renderable {
    def this(file: File) = this(file, (file, int) => println("clicked, but is still using the default event."))
    var height: Int = 12 //+ 4 + 4 //12 is 4 px gap between all three lines 4 is 2 px gap between outer rectangle other 4 is 2px rectangle
    val meta = MapUtils.getMeta(file)
    val authorName = {
      var tempName: String = "Invalid File"
      meta match {
        case Some(m) => if (m.author.length <= charactersPerLine) {
          tempName = m.author
        } else {
          tempName = m.author.substring(0, charactersPerLine - 3).concat("...")
        }
        case None =>
      }
      height += Config.castleSelectFont.getHeight(tempName)
      tempName
    }
    val castleName = {
      var tempName: String = "Invalid File"
      meta match {
        case Some(m) => if (m.name.length <= charactersPerLine) {
          tempName = m.name
        } else {
          tempName = m.name.substring(0, charactersPerLine - 3).concat("...")
        }
        case None =>
      }
      height += Config.castleSelectFont.getHeight(tempName)
      tempName
    }
    val description = {
      var tempName: String = "Invalid File"
      meta match {
        case Some(m) => if (m.description.length <= charactersPerLine) {
          tempName = m.description
        } else {
          tempName = m.description.substring(0, charactersPerLine - 3).concat("...")
        }
        case None =>
      }
      height += Config.castleSelectFont.getHeight(tempName)
      tempName
    }
    var isSelected = false
    var position = new Vector2f(0, 0)

    override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
      if (isSelected) {
        if (container.getInput().isKeyPressed(Input.KEY_E)) {
          onClick(file, Input.KEY_E)
        } else if (container.getInput().isKeyPressed(Input.KEY_ENTER) || container.getInput().isKeyPressed(Input.KEY_SPACE)) {
          onClick(file, Input.KEY_ENTER)
        }
      }
    }

    override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
      var yOffset = position.y

      HUD.castleRow.draw(position.x, yOffset)
      Config.castleSelectFont.drawString(position.x + 10, yOffset + 4, "Creator: " + authorName, if (isSelected) colorSelected else colorNotselected)
      yOffset = yOffset + Config.castleSelectFont.getHeight(authorName) + 4
      Config.castleSelectFont.drawString(position.x + 10, yOffset - 1, "Title: " + castleName, if (isSelected) colorSelected else colorNotselected)
      yOffset = yOffset + Config.castleSelectFont.getHeight(castleName) + 4
      Config.castleSelectFont.drawString(position.x + 10, yOffset - 6, "Desc: " + description, if (isSelected) colorSelected else colorNotselected)
      yOffset = yOffset + Config.castleSelectFont.getHeight(authorName)
    }
  }
}

