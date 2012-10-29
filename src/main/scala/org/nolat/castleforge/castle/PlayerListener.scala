package org.nolat.castleforge.castle

trait PlayerListener {
  def onPlayerEnter(player: Player, srcFloor: Floor) {
    println("default onPlayerEnter")
    //println("Player entered tile from " + srcFloor.itemName)
  }

  def onPlayerExit(player: Player, destFloor: Floor) {
    println("default onPlayerExit")
    //println("Player leaving tile towards " + destFloor.itemName)
  }
}