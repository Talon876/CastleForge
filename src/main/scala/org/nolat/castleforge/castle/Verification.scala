package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Teleporter
import org.nolat.castleforge.castle.items.attributes.IDColor
import org.nolat.castleforge.castle.items.SpawnPoint

object Verification {
  def verify(castle: Castle): (Boolean, List[String]) = {
    var results: List[String] = verifyTeleporters(castle)
    results = verifyStartPosition(castle) ::: results
    (results.isEmpty, results)
  }

  def verifyStartPosition(castle: Castle): List[String] = {
    var results: List[String] = List()
    val spawnPoint = castle.map.flatten.toList.filter {
      floor =>
        floor.item match {
          case Some(itm) => itm match {
            case st: SpawnPoint => true
            case _ => false
          }
          case None => false
        }
    }
    var error: String = ""
    if (spawnPoint.length == 0) {
      error = "Add a spawn point"
      results = error :: results //prepend error only if there is one

    } else if (spawnPoint.length > 1) {
      error = "Only one spawn point can be placed"
      results = error :: results
    }
    results
  }
  def verifyTeleporters(castle: Castle): List[String] = {
    var results: List[String] = List()
    val senders: List[Floor] = findAllTeleportersOfType(castle, "sender")
    val receivers: List[Floor] = findAllTeleportersOfType(castle, "receiver")
    val bidirectionals: List[Floor] = findAllTeleportersOfType(castle, "bidirectional")
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
          val error = "(" + IDColor.toString(clr) + ") Bidirectional and sender/receiver teleporters placed"
          results = error :: results
        } else if (bidirectionalClr.length == 1) {
          val error = "(" + IDColor.toString(clr) + ") Only one bidirectional teleporter."
          results = error :: results
        } else if (bidirectionalClr.length > 2) {
          val error = "(" + IDColor.toString(clr) + ") Too many bidirectional teleporters."
          results = error :: results
        }
      } else {
        if (sendersClr.length == 1) {
          if (recieverClr.length == 0) {
            val error = "(" + IDColor.toString(clr) + ") No receiver teleporter."
            results = error :: results
          } else if (recieverClr.length > 1) {
            val error = "(" + IDColor.toString(clr) + ") Too many receiver teleporters."
            results = error :: results
          }
        } else if (sendersClr.length > 1) {
          val error = "(" + IDColor.toString(clr) + ") Too many sender teleporters."
          results = error :: results
        } else {
          if (recieverClr.length > 0) {
            val error = "(" + IDColor.toString(clr) + ") Receiver teleporter without a sender."
            results = error :: results
          }
        }
      }
    }
    results
  }

  private def findAllTeleportersOfType(castle: Castle, typ: String): List[Floor] = {
    castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(itm) => itm match {
          case t: Teleporter => if (t.teleType == typ) true else false
          case _ => false
        }
        case None => false
      }
    }
  }
}