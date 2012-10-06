package org.nolat.castleforge.graphics

import org.newdawn.slick.Animation
import org.nolat.castleforge.Config
object Sprites extends Enumeration {
  type Sprite = Value
  val checkpoint = Value("checkpoint")
  val crystal_ball = Value("crystal_ball")
  val door = Value("door")
  val endpoint = Value("endpoint")
  val floor = Value("floor")
  val ice = Value("ice")
  val key = Value("key")
  val matchh = Value("match")
  val obstacle = Value("obstacle")
  val player = Value("player")
  val pusher = Value("pusher")
  val sign = Value("sign")
  val spawnpoint = Value("spawnpoint")
  val teleporter = Value("teleporter")
  val torch = Value("torch")
  val wall = Value("wall")

  implicit def value2string(value: Value) = value.toString
}

class Sprite(name: String) {
  private val animDataMap = Config.animationData(name)
  private val spriteSheet = Config.spritesheets(name)

  val animationList = animDataMap.keys.toList
  animationList.foreach(animation => println("Loaded animation " + animation + " for sprite " + name))
  private var currentAnimation = animationList(0)

  private def animationData = animDataMap(currentAnimation)
  private def numberOfFrames = (animationData.end - animationData.start) + 1
  private def frameNumberToFrameCoords(frameNumber: Int) = (frameNumber % spriteSheet.getHorizontalCount, frameNumber / spriteSheet.getHorizontalCount)
  private def startFrame = frameNumberToFrameCoords(animationData.start)
  private def endFrame = frameNumberToFrameCoords(animationData.end)
  private def calculateFrameArray: Array[Int] = {
    animationData.start.to(animationData.end).map(frame =>
      List(frameNumberToFrameCoords(frame)_1, frameNumberToFrameCoords(frame)_2)).toList.flatten.toArray[Int]
  }
  private def calculateFpsArray: Array[Int] = {
    Array.fill(numberOfFrames)(1000 / animationData.fps)
  }

  //  println("width in tiles: " + spriteSheet.getHorizontalCount())
  //  println("height in tiles: " + spriteSheet.getVerticalCount())
  //  animationList.foreach { animation =>
  //    currentAnimation = animation
  //    println("Set currentAnimation for " + name + " to " + currentAnimation)
  //    println(numberOfFrames + " frames of animation at " + animationData.fps + " fps")
  //    println("Start frame coords: " + startFrame)
  //    println("End frame coords: " + endFrame)
  //    calculateFrameArray.foreach(x => print(x + " ")); println
  //    calculateFpsArray.foreach(x => print(x + " "))
  //    println()
  //  }
  //
  private val animations = animationList.map { animation =>
    currentAnimation = animation
    (animation, new Animation(spriteSheet, calculateFrameArray, calculateFpsArray))
  }.toMap

  def getAnimation(): Animation = {
    animations(currentAnimation)
  }

  def setAnimation(newAnimation: String) = {
    if (!animations.contains(newAnimation)) {
      println("Warning! " + newAnimation + " doesn't exist for the sprite " + name + ". Check the meta.xml")
    }
    currentAnimation = newAnimation
  }

}