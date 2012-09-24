package org.nolat.falldown

import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import java.util.Timer
import java.util.TimerTask

class Falldown(title: String) extends BasicGame(title) {

  val player = new Player()

  def init(container: GameContainer) = {
    Config.init() //initialize
    container.setShowFPS(true)

    PlatformManager.spawnPlatform(600)
    val newPlatformTimer = new Timer("NewPlatformSpawner")
    val newPlatformTask = new TimerTask() {
      override def run() = {
        PlatformManager.spawnPlatform(Config.Resolution.getY() + 20)
        player.setScore(player.getScore() + Config.Difficulty)
      }
    }
    newPlatformTimer.schedule(newPlatformTask, Config.WallSpawn, Config.WallSpawn)
    container.setVSync(false)

  }

  override def update(gc: GameContainer, delta: Int) = {
    player.update(gc, delta)
    PlatformManager.update(gc, delta)
  }

  override def render(gc: GameContainer, g: Graphics) = {
    Config.Background.draw(0, 0, Config.Resolution.getX(), Config.Resolution.getY())
    PlatformManager.render(gc, g)
    player.render(g)
  }
}