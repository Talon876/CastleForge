package org.nolat.castleforge

import java.awt.Font

import org.lwjgl.util.Point
import org.newdawn.slick.Image
import org.newdawn.slick.TrueTypeFont
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites

object Config {
  val Title = "CastleForge"
  val Resolution = new Point(1280, 720)
  val Fullscreen = false
  val TileWidth = 64
  val TileHeight = 64
  val DefaultAnimFps = 10
  private val augustaFont = Font.createFont(Font.TRUETYPE_FONT, Config.getClass.getResourceAsStream("/fonts/Augusta.ttf"))
  def animationXsd = getClass.getResourceAsStream("/xsd/CastleForgeSprite.xsd")
  def mapXsd = getClass.getResourceAsStream("/xsd/CastleForgeMap.xsd")

  var UIFont: TrueTypeFont = null
  var TitleScreenBackground: Image = null

  val spriteList = List("teleporter", "checkpoint")

  var sprites: Map[String, Sprite] = null

  def init() = {
    TitleScreenBackground = new Image("images/titlescreen.png")
    UIFont = new TrueTypeFont(augustaFont.deriveFont(Font.PLAIN, 48), true)

    sprites = Sprites.values.map { sprite => (sprite.toString, new Sprite(sprite)) } toMap

  }
}