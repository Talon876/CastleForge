package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Key
import org.nolat.castleforge.castle.items.attributes.Quantity
import org.nolat.castleforge.castle.items.CrystalBall
import org.nolat.castleforge.castle.items.Match
import org.nolat.castleforge.castle.items.attributes.Collectable

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
      case addItmQ: Item with Quantity => {
        this.flatten.map { item =>
          item match {
            case itm: Collectable with Quantity =>
              if (itm.isSimilar(addItmQ)) {
                itm.quantity += addItmQ.quantity
                return
              }
            case _ =>
          }
        }
        this += Some(addItmQ)
      }
      case addItm: Item => {
        this.flatten.map { item =>
          item match {
            case itm: Collectable =>
              if (itm.isSimilar(addItm)) {
                return
              }
            case _ =>
          }
        }
        this += Some(addItm)
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

  def containsItem(itm : Collectable): Boolean = {
    val hasItm = this.filter{ p =>
      {
    	  p match {
    	    case Some(i) => i.isSimilar(itm)
    	    case None => false
    	  }
      }
    }
    if(hasItm.size > 0){true}
    else{false}
  }

  def decrementItem(removeItem: Collectable) {
    decrementItem(removeItem, 1)
  }
  def decrementItem(removeItem: Collectable, amt: Int) {
    if (amt <= 0) { return }
    removeItem match {
      case rmItmQ: Item with Quantity => {
        this.zipWithIndex.map { item =>
          item._1 match {
            case Some(itm: Collectable with Quantity) =>
              if (itm.isSimilar(rmItmQ)) {
                if (itm.quantity > amt) {
                  itm.quantity -= amt
                } else {
                  this.remove(item._2)
                }
                return
              }
            case _ =>
          }
        }
      }
      case rmItm: Item => {
        this.zipWithIndex.map { item =>
          item._1 match {
            case Some(itm: Collectable) =>
              if (itm.isSimilar(rmItm)) {
                this.remove(item._2)
                return
              }
            case _ =>
          }
        }
      }
    }
  }
  def getKeys = {
    this.flatten.map { item =>
      item match {
        case itm: Key => Some(itm)
        case _ => None
      }
    }.flatten.toList
  }

  def getMatch = {
    val matches = this.flatten.map { item =>
      item match {
        case itm: Match => Some(itm)
        case _ => None
      }
    }.flatten.toList
    if (matches.size > 0) Some(matches(0)) else None
  }

  def getCrystalBall = {
    val cballs = this.flatten.map { item =>
      item match {
        case itm: CrystalBall => Some(itm)
        case _ => None
      }
    }.flatten.toList
    if (cballs.size > 0) Some(cballs(0)) else None
  }

  def hasCrystalBall = getCrystalBall.size > 0

  def getMiscItems = {
    val mList = getMatch match {
      case Some(m) => List(m)
      case None => Nil
    }

    val cList = getCrystalBall match {
      case Some(c) => List(c)
      case None => Nil
    }

    mList ::: cList ::: Nil
  }

}