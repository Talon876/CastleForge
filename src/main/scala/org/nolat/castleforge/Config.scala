package org.nolat.castleforge

import java.awt.Font
import org.lwjgl.util.Point
import org.newdawn.slick.Image
import org.newdawn.slick.TrueTypeFont
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import org.nolat.castleforge.graphics.Loader
import org.nolat.castleforge.graphics.AnimationData
import org.newdawn.slick.SpriteSheet
import scala.collection.parallel.immutable.ParMap
import org.nolat.castleforge.xml.SpriteLoad
import scala.util.Random

object Config {
  val Title = "CastleForge"
  val Resolution = new Point(1280, 720)
  val Fullscreen = false
  val TileWidth = 64
  val TileHeight = 64
  val DefaultAnimFps = 10
  val DefaultCastleSize = Tuple2[Int, Int](11, 11)
  val random = new Random()
  val WorkingDirectory = System.getProperty("user.home") + "/." + App.APPNAME.toLowerCase
  private val augustaFont = Font.createFont(Font.TRUETYPE_FONT, Config.getClass.getResourceAsStream("/fonts/Augusta.ttf"))
  def animationXsd = getClass.getResourceAsStream("/xsd/CastleForgeSprite.xsd")
  def mapXsd = getClass.getResourceAsStream("/xsd/CastleForgeMap.xsd")

  var UIFont: TrueTypeFont = null
  var TitleScreenBackground: Image = null

  var animationData: Map[String, Map[String, AnimationData]] = null
  var spritesheets: Map[String, SpriteSheet] = null

  def init() = {
    TitleScreenBackground = new Image("images/titlescreen.png")
    UIFont = new TrueTypeFont(augustaFont.deriveFont(Font.PLAIN, 48), true)

    animationData = Sprites.values.map { sprite => (sprite.toString, Loader.getAnimData(sprite)) }.toMap
    spritesheets = Sprites.values.map { sprite => (sprite.toString, Loader.getSpriteSheet(sprite)) }.toMap

  }
}