package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Inventory extends ArrayBuffer[Item]{
  
  def this(items : Seq[Item])
  {
    this()
    this.appendAll(items)
  }
}