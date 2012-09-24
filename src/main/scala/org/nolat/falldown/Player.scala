package org.nolat.falldown

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

import scala.collection.JavaConversions._

class Player {

  var position = new Vector2f(400, 64)
  var color = Config.PlayerColor
  var size = 32
  var speedMod = 1f
  var score = 0

  def update(gc: GameContainer, delta: Int) = {
    if (position.x - size / 2 < 0) {
      position.x = size / 2
    } else if (position.x > Config.Resolution.getX() - size / 2) {
      position.x = Config.Resolution.getX() - size / 2
    }

    if (position.y > Config.Resolution.getY() - size / 2) {
      position.y = Config.Resolution.getY() - size / 2
    }

    val input = gc.getInput()

    if (input.isKeyDown(Input.KEY_RIGHT)) {
      position.x += Config.PlayerSpeed * speedMod
    } else if (input.isKeyDown(Input.KEY_LEFT)) {
      position.x -= Config.PlayerSpeed * speedMod
    }

    if (checkWalls()) {
      position.y -= Config.WallSpeed
      if (position.y < 0) {
        position.y = 350
        setScore(0)
      }
    } else {
      position.y += Config.Gravity
    }
  }

  def checkWalls() = {
    var wallBelowPlayer = false
    val closeWalls = new ArrayList[Vector2f]()

    //put all closeby wall segments in a list
    PlatformManager.platforms.foreach { platform =>
      platform.walls.foreach { segment =>
        val temp = new Vector2f(segment, platform.positionY)
        closeWalls.add(temp)
      }
    }

    //remove walls above player
    if (closeWalls.size() > 0) {
      for (i <- (closeWalls.size() - 1).to(0).by(-1)) {
        if (closeWalls.get(i).getY() < position.y) {
          closeWalls.remove(closeWalls.get(i))
        }
      }
    }

    //determine if there is a wall right below the player
    if (closeWalls.size() > 0) {
      closeWalls.foreach { wall =>
        if ((position.y - size / 2) < wall.y && (wall.y - (position.y - size / 2) < 26)) {
          if (position.x + size / 2 > wall.x
            && position.x - size / 2 < wall.x + Config.WallTexture.getWidth()) {
            wallBelowPlayer = true;
            wallBelowPlayer
          }
        }
      }
    }
    wallBelowPlayer
  }

  def render(g: Graphics) = {
    Config.PlayerTexture.draw(position.x - size / 2, position.y - size / 2, size, size)
    g.drawString("Score: " + score, 20, 20)
  }

  def getScore() = score

  def setScore(score: Int) = this.score = score
}