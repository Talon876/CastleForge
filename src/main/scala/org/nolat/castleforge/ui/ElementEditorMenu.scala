package org.nolat.castleforge.ui

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.states.SharedStateData
import org.nolat.castleforge.states.CastleLoading
import org.newdawn.slick.state.transition.EmptyTransition
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition
import org.nolat.castleforge.states.MainMenuScreen
import org.nolat.castleforge.xml.MapSave
import org.nolat.castleforge.Config

class ElementEditorMenu(castle: Castle, game: StateBasedGame) extends HUDElement(HUD.custom) {

  val pauseMenu = new Menu(new Vector2f(8 + 64 * 2, 8 + 64 * 2))
  pauseMenu.add("Save", handleMenu)
  pauseMenu.add("Main Menu", handleMenu)
  pauseMenu.add("Cancel", handleMenu)
  private var lastPaused = false

  private var container: GameContainer = null

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.isPaused && !lastPaused) { //was just paused
      container.getInput().clearKeyPressedRecord() //clear keys pressed so the menu doesn't thing w/s was pressed
    }

    if (container.isPaused) pauseMenu.update(container, game, delta)
    this.container = container
    lastPaused = container.isPaused
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (container.isPaused) {
      g.setColor(new Color(255, 255, 255, 128))
      g.fillRoundRect(position.x, position.y, 64f * 11f, 64f * 11f, 16)
      g.setColor(Color.black)
      pauseMenu.render(container, game, g)
    }
  }

  private def handleMenu(text: String, position: Vector2f) {
    text match {
      case "Save" => {
        println("saving map")
        castle.authorName = "TestBro"
        castle.name = "Castle" + Config.random.nextInt(10000)
        castle.description = "A castle with " + castle.map.flatten.size + " tiles."
        MapSave.save(castle, true, Some(castle.fileLocation))
      }
      case "Main Menu" => {
        container.setPaused(false)
        game.enterState(MainMenuScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition())
      }
      case "Cancel" => {
        container.setPaused(false)
      }
    }
  }
}