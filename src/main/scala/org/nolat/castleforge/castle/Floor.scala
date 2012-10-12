package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Item

class Floor(val item: Option[Item]) {

  def canEnter(): Boolean = {
    true
  }
}