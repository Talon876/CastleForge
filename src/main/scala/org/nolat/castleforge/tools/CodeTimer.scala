package org.nolat.castleforge.tools

object CodeTimer {
  //used to quickly test how long a section of code takes to run
  private var startTime: Long = 0

  def start() {
    startTime = System.currentTimeMillis()
  }

  def finish() {
    val now = System.currentTimeMillis()
    println("Took " + (now - startTime) + "ms")
  }

}