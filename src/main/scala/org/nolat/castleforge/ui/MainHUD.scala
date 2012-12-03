package org.nolat.castleforge.ui

import org.nolat.castleforge.castle.Player
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.Input
import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config

class MainHUD(castle: Castle, game: StateBasedGame) extends HUD {

  val grooves = new HUDElement(HUD.grooves)
  grooves.position = new Vector2f(728, 8)
  this add grooves

  val logoBG = new HUDElement(HUD.grooves)
  logoBG.position = new Vector2f(728, Config.Resolution.getY - HUD.grooves.getHeight)
  this add logoBG

  val logo = new HUDElement(HUD.logo)
  logo.position = new Vector2f(728, 588)
  this add logo

  val borders = new HUDElement(HUD.border)
  this add borders

  val playerDebug = new ElementPlayerDebug(castle.player)
  playerDebug.position = new Vector2f(8, 8 + 64 * 9)
  this add playerDebug

  val signElement = new ElementSign(castle.player)
  signElement.position = new Vector2f(54, 100)
  this add signElement

  val playerInventory = new ElementInventory(castle.player)
  playerInventory.position = new Vector2f(grooves.position.x + 16, grooves.position.y + 16)
  this add playerInventory

  val pauseMenu = new ElementPauseMenu(castle, game)
  pauseMenu.position = new Vector2f(8, 8)
  this add pauseMenu

  def toggleDebug() = playerDebug.toggle()

  def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_F3) {
      toggleDebug()
    }
  }
}