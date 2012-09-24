package org.nolat.castleforge

import org.lwjgl.util.Point
import org.newdawn.slick.Color
import org.newdawn.slick.Image

object Config {
  val Title = "Falldown"
  val Resolution = new Point(800, 600)
  val Fullscreen = false

  val Difficulty = 5

  val PlayerColor = Color.white
  val PlayerSpeed = 2.55f
  val WallSpeed = 1.15f
  val Gravity = 1.75f
  val WallSpawn = 800

  var WallTexture: Image = null
  var Background: Image=null
  var PlayerTexture: Image=null

  def init() = {
    WallTexture = new Image("images/wallsegment.png")
    Background = new Image("images/background.jpg")
    PlayerTexture = new Image("images/ball.png")
  }
}