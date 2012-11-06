package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Key
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.castle.items.CrystalBall
import org.nolat.castleforge.castle.items.Match
import org.nolat.castleforge.castle.items.Collectable

class Inventory extends ArrayBuffer[Option[Collectable]] {
  def this(items: Seq[Option[Collectable]]) {
    this()
    this.appendAll(items)
  }

  //This should be comparing the items properly using Collectable type
  //all in one method because of http://stackoverflow.com/questions/2510108/why-avoid-method-overloading and
  //http://stackoverflow.com/questions/1094173/how-do-i-get-around-type-erasure-on-scala-or-why-cant-i-get-the-type-paramete
  def addItem(addedItem: Collectable) {
    addedItem match {
      case addItmQ: Item with Quantity =>
        {
          val itemsType = this.flatten.map { item =>
            item match {
              case itm: Collectable with Quantity =>
                if (itm.equalCollectable(addedItem)) {
                  Some(itm)
                } else {
                  None
                }
              case _ => None
            }
          }.flatten
          if (itemsType.size == 1) {
            itemsType(0).quantity += addItmQ.quantity
          } else if (itemsType.size == 0) {
            this += Some(addItmQ)
          } else if (itemsType.size > 1) {
            assert(itemsType.size == 1)
          }
        }
      case addItm: Item =>
        {
          val itemsType = this.flatten.map { item =>
            item match {
              case itm: Collectable =>
                if (itm.equalCollectable(addedItem)) {
                  Some(itm)
                } else {
                  None
                }
              case _ => None
            }
          }.flatten
          if (itemsType.size == 0) {
            this += Some(addItm)
          } else if (itemsType.size > 1) {
            assert(itemsType.size == 1)
          }
        }
    }

  }
  def addItems(addedItem: Collectable*) {
    addedItem.foreach { itm =>
      itm match {
        case collect: Collectable => addItem(collect)
      }
    }
  }
  def getKeys = {
    this.flatten.map { item =>
      item match {
        case itm: Key => Some(itm)
        case _ => None
      }
    }.flatten
  }
  def getMatches = {
    this.flatten.map { item =>
      item match {
        case itm: Match => Some(itm)
        case _ => None
      }
    }.flatten
  }
  def getCrystalBall = {
    this.flatten.map { item =>
      item match {
        case itm: CrystalBall => Some(itm)
        case _ => None
      }
    }.flatten
  }
}