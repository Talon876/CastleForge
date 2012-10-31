package org.nolat.castleforge.castle

trait PlayerListener {
  def onPlayerEnter(player: Player, srcFloor: Floor) {
    //println("Player entered tile from " + srcFloor.itemName)
  }

  def onPlayerExit(player: Player, destFloor: Floor) {
    //println("Player leaving tile towards " + destFloor.itemName)
  }
}