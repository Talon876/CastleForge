package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.Item

class Inventory extends ArrayBuffer[Option[Item]] {

  def this(items: Seq[Option[Item]]) {
    this()
    this.appendAll(items)
  }
}