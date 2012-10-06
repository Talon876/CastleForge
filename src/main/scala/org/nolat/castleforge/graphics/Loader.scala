package org.nolat.castleforge.graphics

import org.newdawn.slick.Image
import org.nolat.castleforge.xml.SpriteLoad
import org.nolat.castleforge.Config
import org.newdawn.slick.SpriteSheet

object Loader {

  def getAnimData(spriteName: String): Map[String, AnimationData] = {
    val meta = SpriteLoad.loadSprite(getClass.getResourceAsStream("/sprites/" + spriteName + "/meta.xml"))

    val animationMap = meta.animation.map { anim =>
      val name = anim.name
      val start = anim.start.toInt
      val end = anim.end match {
        case Some(end) => end.toInt
        case None => start
      }
      val fps = anim.fps match {
        case Some(fps) => fps.toInt
        case None => Config.DefaultAnimFps
      }
      val loop = anim.loop.getOrElse(false)
      (name, AnimationData(start, end, fps, loop))
    }.toMap
    animationMap
  }

  def getSpriteSheet(spriteName: String): SpriteSheet = {
    new SpriteSheet("sprites/" + spriteName + "/" + spriteName + ".png", Config.TileWidth, Config.TileHeight)
  }
}