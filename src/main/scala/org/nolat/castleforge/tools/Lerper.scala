package org.nolat.castleforge.tools

import scala.collection.mutable.ListBuffer
object Lerper {
  val lerpers = new ListBuffer[Lerper]
  def apply(msToLerp: Int) = new Lerper(msToLerp)

  def lerp(val1: Float, val2: Float, amount: Float) = {
    if (amount < 0f) {
      val1
    }
    if (amount > 1f) {
      val2
    }
    if (val1 == val2) {
      val2
    } else {
      val1 + (val2 - val1) * amount
    }
  }
}
class Lerper(var msToLerp: Int) {
  Lerper.lerpers += this

  var elapsedMillis = 0f
  var start = 0f
  var finish = 0f

  private var isStarted = false

  def start(start: Float, finish: Float) {
    this.start = start
    this.finish = finish
    this.isStarted = true
  }

  def update(deltaMs: Int) {
    if (isStarted) {
      println("elapsed: " + elapsedMillis)
      elapsedMillis += deltaMs
      if (elapsedMillis >= msToLerp) {
        finishedLerp()
        this.isStarted = false
        elapsedMillis = 0
      }
    }
  }

  def value = {
    Lerper.lerp(start, finish, amountLerped)
  }

  def amountLerped = elapsedMillis / msToLerp

  def delete() = Lerper.lerpers.remove(Lerper.lerpers.indexOf(this))

  var finishedLerp: () => Unit = { () => println("finished lerp") }

}