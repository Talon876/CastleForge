package org.nolat.castleforge

import org.lwjgl.util.Point
import org.newdawn.slick.Color
import org.newdawn.slick.Image

object Config {
  val Title = "CastleForge"
  val Resolution = new Point(1280, 720)
  val Fullscreen = false

  val Difficulty = 5

  val PlayerColor = Color.white
  val PlayerSpeed = 2.55f
  val WallSpeed = 1.15f
  val Gravity = 1.75f
  val WallSpawn = 800

  var TitleScreenBackground: Image = null

  def init() = {
    TitleScreenBackground = new Image("images/titlescreen.png")
  }
}