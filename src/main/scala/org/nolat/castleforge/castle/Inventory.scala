package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.Item

class Inventory extends ArrayBuffer[Item] {

  def this(items: Seq[Item]) {
    this()
    this.appendAll(items)
  }
}