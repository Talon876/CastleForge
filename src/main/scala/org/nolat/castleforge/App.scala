package org.nolat.castleforge
import java.io.IOException;
import java.util.Properties;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
object App {

  val VERSION = "1.0-SNAPSHOT"
  val APPNAME = "CastleForge"

  def main(args: Array[String]) = {
    println("Scala Version")
    val nativeDir = NativeExtractor.extractNatives()
    println("Set org.lwjgl.librarypath to " + nativeDir)
    System.setProperty("org.lwjgl.librarypath", nativeDir)
    try {
      val app = new AppGameContainer(new CastleForge(APPNAME + " - " + VERSION))
      app.setDisplayMode(Config.Resolution.getX(), Config.Resolution.getY(), Config.Fullscreen)
      app.start()
    } catch {
      case e: SlickException => e.printStackTrace()
    }
  }
}
