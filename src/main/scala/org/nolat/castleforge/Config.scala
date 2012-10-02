package org.nolat.castleforge

import org.lwjgl.util.Point
import org.newdawn.slick.Color
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.Animation

object Config {
  val Title = "CastleForge"
  val Resolution = new Point(1280, 720)
  val Fullscreen = false

  val TileWidth = 64
  val TileHeight = 64

  var TitleScreenBackground: Image = null
  var crystalBallSprite: SpriteSheet = null
  var teleporterSS: SpriteSheet = null

  var testAnim: Animation = null
  var teleAnim: Animation = null

  def init() = {
    TitleScreenBackground = new Image("images/titlescreen.png")
    crystalBallSprite = new SpriteSheet("sprites/torch/torch.png", TileWidth, TileHeight)
    teleporterSS = new SpriteSheet("sprites/teleporter/teleporter.png", TileWidth, TileHeight)
    val range = (0 to 3).toArray

    testAnim = new Animation(crystalBallSprite, Array(0, 0, 1, 0, 2, 0), Array(500, 500, 500, 500))
    teleAnim = new Animation(teleporterSS, Array(0, 0, 1, 0, 2, 0, 3, 0), Array(100, 100, 100, 100))
  }
}