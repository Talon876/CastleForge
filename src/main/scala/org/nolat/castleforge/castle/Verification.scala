package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Teleporter
import org.nolat.castleforge.castle.items.attributes.IDColor

object Verification {
  def verify(castle: Castle): (Boolean, List[String]) = {
    var results: List[String] = verifyTeleporters(castle: Castle)
    (results.isEmpty, results)
  }
  def verifyTeleporters(castle: Castle): List[String] = {
    var results: List[String] = List()
    val senders: List[Floor] = findAllSenderTeleporters(castle)
    val receivers: List[Floor] = findAllReceiverTeleporters(castle)
    val bidirectionals: List[Floor] = findAllBidirectionalTeleporters(castle)
    IDColor.values.foreach { clr =>
      val sendersClr: List[Floor] = senders.filter { flr =>
        flr.item match {
          case Some(i: Teleporter) => i.idcolor == clr
          case None => false
        }
      }
      val recieverClr: List[Floor] = receivers.filter { flr =>
        flr.item match {
          case Some(i: Teleporter) => i.idcolor == clr
          case None => false
        }
      }
      val bidirectionalClr: List[Floor] = bidirectionals.filter { flr =>
        flr.item match {
          case Some(i: Teleporter) => i.idcolor == clr
          case None => false
        }
      }
      if (bidirectionalClr.length > 0) {
        if (sendersClr.length > 0 || recieverClr.length > 0) { //If there is any bidirectional teles there cannot be senders/receivers of that color
          val error = "Bidirectional teleporter(s) and a sender and/or receiver of color" + IDColor.toString(clr)
          results = error :: results
        } else if (bidirectionalClr.length == 1) {
          val error = "Only one bidirectional teleporter of " + IDColor.toString(clr)
          results = error :: results
        } else if (bidirectionalClr.length > 2) {
          val error = "Too many bidirectional teleporters of color " + IDColor.toString(clr)
          results = error :: results
        }
      } else {
        if (sendersClr.length == 1) {
          if (recieverClr.length == 0) {
            val error = "No receiver teleporter of color " + IDColor.toString(clr)
            results = error :: results
          } else if (recieverClr.length > 1) {
            val error = "Too many receiver teleporters of color " + IDColor.toString(clr)
            results = error :: results
          }
        } else if (sendersClr.length > 1) {
          val error = "Too many sender teleporters of color " + IDColor.toString(clr)
          results = error :: results
        } else {
          if (recieverClr.length > 0) {
            val error = "Receiver teleporter(s) without sender of color " + IDColor.toString(clr)
            results = error :: results
          }
        }
      }
    }
    results
  }
  private def findAllSenderTeleporters(castle: Castle): List[Floor] = {
    castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(i: Teleporter) => if (i.teleType == "sender") { true } else { false }
        case None => false
      }
    }
  }
  private def findAllReceiverTeleporters(castle: Castle): List[Floor] = {
    castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(i: Teleporter) => if (i.teleType == "receiver") { true } else { false }
        case None => false
      }
    }
  }
  private def findAllBidirectionalTeleporters(castle: Castle): List[Floor] = {
    castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(i: Teleporter) => if (i.teleType == "bidirectional") { true } else { false }
        case None => false
      }
    }
  }
}