package org.nolat.castleforge

import org.lwjgl.util.Point
import org.newdawn.slick.Color
import org.newdawn.slick.Image
import org.newdawn.slick.SpriteSheet
import org.newdawn.slick.Animation
import org.newdawn.slick.UnicodeFont
import java.awt.Font
import java.io.File
import org.newdawn.slick.font.effects.ColorEffect
import java.awt.{ Color => JColor }
import org.newdawn.slick.font.effects.Effect
import scala.collection.mutable.MutableList
import scala.collection.JavaConversions._
import java.util.ArrayList
import org.newdawn.slick.TrueTypeFont
import java.awt.GraphicsEnvironment

object Config {
  val Title = "CastleForge"
  val Resolution = new Point(1280, 720)
  val Fullscreen = false

  val TileWidth = 64
  val TileHeight = 64

  private val augustaFont = Font.createFont(Font.TRUETYPE_FONT, Config.getClass().getResourceAsStream("/fonts/Augusta.ttf"))

  var UIFont: TrueTypeFont = null
  var TitleScreenBackground: Image = null
  var crystalBallSprite: SpriteSheet = null
  var teleporterSS: SpriteSheet = null

  var testAnim: Animation = null
  var teleAnim: Animation = null

  def init() = {
    TitleScreenBackground = new Image("images/titlescreen.png")
    crystalBallSprite = new SpriteSheet("sprites/torch/torch.png", TileWidth, TileHeight)
    teleporterSS = new SpriteSheet("sprites/teleporter/teleporter.png", TileWidth, TileHeight)
    UIFont = new TrueTypeFont(augustaFont.deriveFont(Font.PLAIN, 48), true)

    testAnim = new Animation(crystalBallSprite, Array(0, 0, 1, 0, 2, 0), Array(500, 500, 500, 500))
    teleAnim = new Animation(teleporterSS, Array(0, 0, 1, 0, 2, 0, 3, 0), Array(100, 100, 100, 100))

  }
}