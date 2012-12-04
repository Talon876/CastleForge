package org.nolat.castleforge.states

import org.newdawn.slick.state.BasicGameState
import org.nolat.castleforge.Config
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.transition.EmptyTransition
import org.newdawn.slick.Color
import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.gui.TextField
import org.newdawn.slick.gui.AbstractComponent
import org.newdawn.slick.gui.ComponentListener
import org.newdawn.slick.gui.MouseOverArea
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.xml.MapSave
import org.newdawn.slick.state.transition.FadeOutTransition
import org.newdawn.slick.state.transition.FadeInTransition
import org.nolat.castleforge.castle.Verification

object SaveScreen {
  val ID = 8
}
class SaveScreen extends BasicGameState with ComponentListener {

  var game: StateBasedGame = null

  override def getID = SaveScreen.ID

  var castle: Castle = null
  var authorTextfield: TextField = null
  var castleTextfield: TextField = null
  var saveButton: MouseOverArea = null
  var backButton: MouseOverArea = null

  var errorList: List[String] = Nil

  def saveLocation = Config.WorkingDirectory + "/maps/" + authorTextfield.getText + "-" + castleTextfield.getText + ".xml"

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def enter(container: GameContainer, game: StateBasedGame) {
    container.setPaused(false)
    castle = SharedStateData.loadedCastle
    errorList = Nil

    authorTextfield = new TextField(container, Config.guiFont, 500, 50, 400, 24)
    castleTextfield = new TextField(container, Config.guiFont, 500, 100, 400, 24)
    saveButton = new MouseOverArea(container, HUD.save, Config.Resolution.getX - HUD.save.getWidth - 8, Config.Resolution.getY - HUD.save.getHeight - 8, HUD.save.getWidth, HUD.save.getHeight, this)

    backButton = new MouseOverArea(container, HUD.back, 8, Config.Resolution.getY - HUD.back.getHeight - 8, HUD.back.getWidth, HUD.back.getHeight, this)
    backButton.setMouseOverImage(HUD.backOver)

    authorTextfield.setText(castle.authorName)
    castleTextfield.setText(castle.name)

    val result = Verification.verify(castle)
    if (result._1) {
      //no errors
      println("no errors")
      saveButton.setMouseOverImage(HUD.saveOver)
    } else {
      println("errors")
      errorList = result._2
    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Config.stonewall.draw(0, 0)
    g.setColor(Color.black)
    Config.UIFont.drawString(220, 30, "Author Name: ")
    Config.UIFont.drawString(240, 80, "Castle Name: ")
    //Config.guiFont.drawString(8, 3, "(Save Location: " + saveLocation + ")")
    g.setColor(Color.white)
    authorTextfield.render(container, g)
    castleTextfield.render(container, g)
    saveButton.render(container, g)
    backButton.render(container, g)

    if (errorList.size > 0) {
      Config.castleSelectFont.drawString(340, 140, "Please fix the following errors before saving")
      g.setColor(Color.lightGray)
      g.drawLine(345, 170, 860, 170)
    }

    errorList.zipWithIndex.foreach {
      case (error, idx) =>
        Config.castleSelectFont.drawString(400, 170 + idx * 33, (idx + 1) + ". " + error)
    }
  }

  override def componentActivated(source: AbstractComponent) {
    if (source.equals(saveButton)) {
      if (errorList.size == 0) {
        castle.authorName = authorTextfield.getText
        castle.name = castleTextfield.getText
        MapSave.save(castle, true, Some(castle.fileLocation))
        game.enterState(MainMenuScreen.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black))
      }
    } else if (source.equals(backButton)) {
      SharedStateData.loadedCastle = castle
      game.enterState(CreateCastleScreen.ID, new FadeOutTransition(Color.black), null)
    }
  }
}