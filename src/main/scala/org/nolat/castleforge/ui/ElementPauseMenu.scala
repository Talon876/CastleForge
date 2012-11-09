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

class ElementPauseMenu(castle: Castle, game: StateBasedGame) extends HUDElement(HUD.custom) {

  val pauseMenu = new Menu(new Vector2f(8 + 64 * 2, 8 + 64 * 2))
  pauseMenu.add("Restart from checkpoint", handleMenu)
  pauseMenu.add("Restart", handleMenu)
  pauseMenu.add("Main Menu", handleMenu)
  //pauseMenu.add("Cancel", handleMenu)
  private var lastPaused = false

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.isPaused && !lastPaused) { //was just paused
      container.getInput().clearKeyPressedRecord() //clear keys pressed so the menu doesn't thing w/s was pressed
    }

    if (container.isPaused) pauseMenu.update(container, game, delta)

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
      case "Restart from checkpoint" => {
        SharedStateData.loadOriginal = false
        SharedStateData.mapFile = castle.fileLocation
        game.enterState(CastleLoading.ID, new EmptyTransition(), new EmptyTransition())
      }
      case "Restart" => {
        SharedStateData.loadOriginal = true
        SharedStateData.mapFile = castle.fileLocation
        game.enterState(CastleLoading.ID, new EmptyTransition(), new EmptyTransition())
      }
      case "Main Menu" => {
        game.enterState(MainMenuScreen.ID, new FadeOutTransition(), new FadeInTransition())
      }
    }
  }
}