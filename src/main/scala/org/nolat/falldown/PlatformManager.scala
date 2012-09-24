package org.nolat.falldown

import java.util.List
import scala.collection.JavaConversions._
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.tools.Toolkit
import java.util.concurrent.CopyOnWriteArrayList

object PlatformManager {
  val platforms: CopyOnWriteArrayList[Platform] = new CopyOnWriteArrayList[Platform]()

  def getPlatforms(): List[Platform] = {
    platforms
  }

  def spawnPlatform(positionY: Int) = {
    val p = new Platform(positionY)
    platforms.add(p)
  }

  def update(gc: GameContainer, delta: Int) = {
    for (i <- (platforms.size() - 1).to(0).by(-1)) {
      platforms.get(i).update(gc, delta);
      if (platforms.get(i).positionY < 0) {
        platforms.remove(platforms.get(i));
      }
    }
  }

  def render(gc: GameContainer, g: Graphics) {
    platforms.foreach { platform =>
      platform.setColor(lerpBetweenColors(platform.colorA, platform.colorB, platform.positionY
        / Config.Resolution.getY()))
      platform.render(gc, g)
    }
  }

  private def lerpBetweenColors(colorA: Color, colorB: Color, amount: Float): Color = {
    val r = Toolkit.lerp(colorA.getRed(), colorB.getRed(), amount);
    val g = Toolkit.lerp(colorA.getGreen(), colorB.getGreen(), amount);
    val b = Toolkit.lerp(colorA.getBlue(), colorB.getBlue(), amount);
    return new Color(r, g, b);
  }
}