package org.nolat.falldown

import scala.collection.JavaConversions._;

import java.util.ArrayList
import java.util.List

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

import org.lwjgl.util._

import org.nolat.tools.Toolkit

class Platform(var positionY: Float) {
  val walls: List[Int] = new ArrayList[Int]()

  var colorA: Color = Color.white
  var colorB: Color = Color.white
  var color: Color = Color.white

  setupWalls()
  def this() = this(390) //This calls the default constructor with a default parameter of 390

  private def setupWalls() {
    colorA = randomColor()
    colorB = inverseColor(colorA)
    var tries: Int = 0
    while (walls.size() < 1 && tries < 10) {

      walls.clear()

      for (i <- 0.to(Config.Resolution.getX()).by(Config.WallTexture.getWidth())) {
        val choice: Int = Toolkit.randomRange(0, Config.Difficulty)
        if (choice != 0) {
          walls.add(i)
        }
      }
      tries += 1
    }
    if (walls.size() >= Config.Resolution.getX() / Config.WallTexture.getWidth() - 2) {
      walls.remove(Toolkit.randomRange(0, walls.size() - 1));
    }
  }

  private def randomColor(): Color = {
    val r: Int = Toolkit.randomRange(0, 255);
    val g: Int = Toolkit.randomRange(0, 255);
    val b: Int = Toolkit.randomRange(0, 255);
    new Color(r, g, b);
  }

  private def inverseColor(color: Color): Color = {
    val inverse: Color = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
    inverse;
  }

  def update(gc: GameContainer, delta: Int) = {
    positionY -= Config.WallSpeed;
  }

  def render(gc: GameContainer, g: Graphics) {
    walls.foreach(wallPosition => Config.WallTexture.draw(wallPosition, positionY, color));
  }

  def setColor(color: Color) = {
    this.color = color;
  }
}