package org.nolat.castleforge.castle.items.attributes

import org.nolat.castleforge.castle.PlayerListener
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.items.Item

trait Collectable extends Item with PlayerListener {

  override def onPlayerEnter(player: Player, srcFloor: Floor) {
    println("Collectable onPlayerEnter")
    player.inventory.addItem(this)
    CastleUtil.removeItem(player.castle, player.tilePosition) //remove item at players position (this)
  }

  def isSimilar(other: Collectable): Boolean = false
}