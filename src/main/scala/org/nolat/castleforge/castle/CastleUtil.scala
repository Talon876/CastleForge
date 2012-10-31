package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items._

object CastleUtil {

  def findSpawnpoint(castle: Castle): Floor = {
    val spawnPoint = castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(x) => x.isInstanceOf[SpawnPoint]
        case None => false
      }
    }
    spawnPoint(0) //if this throws an exception then you don't have a spawn point which isn't valid
  }

  def findMatchingTeleporter(castle: Castle, teleporter: Teleporter): Floor = {

    def matchingTeleporter(sourceTeleporter: Teleporter, candidate: Floor): Boolean = {

      candidate.item match {
        case Some(item) => item match {
          case tele: Teleporter => (tele.idcolor == teleporter.idcolor) && (tele.teleType == "receiver" || tele.teleType == "bidirectional") && tele != sourceTeleporter
          case _ => false
        }
        case None => false
      }
    }

    val matchingTeleporters = castle.map.flatten.toList.filter(floor => matchingTeleporter(teleporter, floor))

    println("found teleporters: " + matchingTeleporters)

    matchingTeleporters(0) //if this throws an exception then you don't have a matching teleporter which isn't valid
  }
}