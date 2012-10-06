package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Inventory extends ArrayBuffer[Item]{
  
  def this(i : org.nolat.castleforge.xml.CastleForgeItemType)
  {
    this()
    this.appendAll(i.item.map(it => new Item()))
  }
}