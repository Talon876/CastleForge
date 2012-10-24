package org.nolat.castleforge.castle

trait PlayerListener {
  def onPlayerEnter(player: Player, srcFloor: Floor) {
  }

  def onPlayerExit(player: Player, destFloor: Floor) {
  }
}