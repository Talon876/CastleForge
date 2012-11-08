package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items._
import org.nolat.castleforge.castle.items.attributes.CheckPointState
import java.lang.Class

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

  def findPlayerStart(castle: Castle): Floor = {
    val candidates = findAllCheckPointStates(castle)
    candidates.filter { floor =>
      floor.item match {
        case Some(x) => x.asInstanceOf[CheckPointState].checkpointstate == CheckPointState.ACTIVE
        case None => false
      }

    }(0) //There can only be one active CheckPointState in a Castle at any given time
  }

  def activateCheckPoint(castle: Castle, chkpnt: CheckPointState) {
    val candidates = findAllCheckPointStates(castle)
    candidates.foreach { floor =>
      floor.item match {
        case Some(x) =>
          val itm = x.asInstanceOf[CheckPointState]
          if (itm != chkpnt) {
            itm.deactivate()
          } else {
            itm.activate()
          }
        case None => false
      }

    }
  }

  def findAllCheckPointStates(castle: Castle): List[Floor] = {
    val checkpoint = castle.map.flatten.toList.filter { floor =>
      floor.item match {
        case Some(x) => x match {
          case itm : CheckPointState => true
          case _ => false
        }
        case None => false
      }
    }
    checkpoint

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

    matchingTeleporters(0) //if this throws an exception then you don't have a matching teleporter which isn't valid
  }

  def removeItem(castle: Castle, coords: (Int, Int)) {
    castle.map(coords._2)(coords._1).item = None
  }

  def distanceBetweenTiles(floor1: Floor, floor2: Floor): Int = distanceBetweenTiles(floor1.getTilePosition, floor2.getTilePosition)

  def distanceBetweenTiles(tile1: (Int, Int), tile2: (Int, Int)): Int = {
    scala.math.floor(
      scala.math.sqrt(
        (tile2._1 - tile1._1) * (tile2._1 - tile1._1) + (tile2._2 - tile1._2) * (tile2._2 - tile1._2))).toInt
  }

  def floorAt(castle: Castle, coords: (Int, Int)): Floor = {
    castle.map(coords._2)(coords._1)
  }

  def itemAt(castle: Castle, coords: (Int, Int)): Option[Item] = {
    castle.map(coords._2)(coords._1).item
  }

  //http://stackoverflow.com/questions/5485817/can-i-perform-matching-on-a-type-parameter-in-scala-to-see-if-it-implements-a-tr
  //for some reason doing <: Item won't work if this is called using classOf[Readable]. probably something to do
  //with it being a trait (even though it extends Item so i'm not sure why)
  def isItemAt(castle: Castle, coords: (Int, Int), clazz: Class[_ <: Item]): Boolean = {
    castle.map(coords._2)(coords._1).item match {
      case Some(x) => x.getClass isAssignableFrom clazz
      case None => false
    }
  }

  def getAllRoomsContainingItem(castle: Castle, clazz: Class[_ <: Item]): List[Floor] = {
    castle.map.flatten.filter(floor => isItemAt(castle, floor.getTilePosition, clazz)).toList
  }

  def getFloorsSharingRoomIds(castle: Castle, floor: Floor, exactMatch: Boolean = true): List[Floor] = {
    getFloorsSharingRoomIds(castle, floor.roomIDs, exactMatch)
  }

  def getFloorsSharingRoomIds(castle: Castle, roomIds: String, exactMatch: Boolean): List[Floor] = {
    castle.map.flatten.filter(floor => floor.sharesRoomId(roomIds, exactMatch)).toList
  }

  def getFloorsSharingRoomIds(castle: Castle, roomIdsList: List[String], exactMatch: Boolean): List[Floor] = {
    roomIdsList.map(roomId => getFloorsSharingRoomIds(castle, roomId, exactMatch)).flatten
  }
}