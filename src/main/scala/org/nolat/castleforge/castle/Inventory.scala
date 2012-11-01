package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Key

class Inventory extends ArrayBuffer[Option[Item]] {

  def this(items: Seq[Option[Item]]) {
    this()
    this.appendAll(items)
  }

  def addItem(item: Item) {
    this += Some(item)
  }

  def getKeys = {
    this.filter { item =>
      item match {
        case Some(i) => i match {
          case k: Key => true
          case _ => false
        }
        case None => false
      }
    }.map { item =>
      item.get.asInstanceOf[Key]
    }
  }
}