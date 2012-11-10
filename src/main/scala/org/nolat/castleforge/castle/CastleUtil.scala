package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items._
import org.nolat.castleforge.castle.items.attributes.CheckPointState
import java.lang.Class
import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.ExpansionDirection._

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
          case itm: CheckPointState => true
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

  /**
   * Will return whether it expanded the castle or not, if false it would have been above the 300x300 bounds in at least one direction
   */
  def expandCastle(castle: Castle, direction: ExpansionDirection, amount: Int): Boolean = {
    direction match {
      case ExpansionDirection.ALL => expandCastleAll(castle, amount)
      case ExpansionDirection.LEFT => expandCastleLeft(castle, amount)
      case ExpansionDirection.RIGHT => expandCastleRight(castle, amount)
      case ExpansionDirection.TOP => expandCastleTop(castle, amount)
      case ExpansionDirection.BOTTOM => expandCastleBottom(castle, amount)
      case ExpansionDirection.NONE => true //don't expand but return true
    }
  }

  def isEdge(castle: Castle, coords: (Int, Int)): ExpansionDirection = {
    val map = castle.map
    val cols = map(0).size
    val rows = map.size
    if (coords._1 + 1 >= cols) {
      ExpansionDirection.RIGHT
    } else if (coords._1 - 1 <= 0) {
      ExpansionDirection.LEFT
    } else if (coords._2 + 1 >= rows) {
      ExpansionDirection.BOTTOM
    } else if (coords._2 - 1 <= 0) {
      ExpansionDirection.TOP
    } else { ExpansionDirection.NONE } //No expansion needed
  }

  /**
   * amount is the number of rows and columns to add to all sides of the current map
   */
  def expandCastleAll(castle: Castle, amount: Int): Boolean = {
    val curRows: Int = castle.map.size
    val curCols: Int = castle.map(0).size
    val newRows: Int = curRows + 2 * amount
    val newCols: Int = curCols + 2 * amount
    if (newCols <= 300 && newRows <= 300) {
      //TODO: this function might require pausing the game container so that the player will not try to move something while the update is happening also because rendering will be messed up
      val newMap = new ArrayBuffer[ArrayBuffer[Floor]]
      val curMap = castle.map
      for (row <- 0 until newRows) {
        newMap.append(new ArrayBuffer[Floor])
        if (row < amount || row >= (newRows - amount)) { //new rows that need to be added
          for (col <- 0 until newCols) {
            newMap(row).append(new Floor(None, col, row))
          }
        } else { //old rows that need columns added to them
          for (col <- 0 until amount) { //new cols added before current columns
            newMap(row).append(new Floor(None, col, row))
          }
          for (col <- amount until (newCols - amount)) { //original tiles
            val f: Floor = curMap((row - amount))((col - amount))
            f.setXY(col, row) //set the new x and y tile positions
            newMap(row).append(f)
          }
          for (col <- (newCols - amount) until newCols) { //new cols added after current columns
            newMap(row).append(new Floor(None, col, row))
          }
        }
      }
      castle.map = newMap
      true
    } else {
      false
    }
  }

  def expandCastleLeft(castle: Castle, amount: Int): Boolean = {
    val curRows: Int = castle.map.size
    val curCols: Int = castle.map(0).size
    val newCols: Int = curCols + amount
    if (newCols <= 300) {
      val newMap = new ArrayBuffer[ArrayBuffer[Floor]]
      val curMap = castle.map
      for (row <- 0 until curRows) {
        newMap.append(new ArrayBuffer[Floor])
        for (col <- 0 until amount) { //new cols added before current columns
          newMap(row).append(new Floor(None, col, row))
        }
        for (col <- amount until newCols) { //from amount added until end
          val f: Floor = curMap(row)((col - amount)) //no row shift columns were shifted right
          f.setXY(col, row) //set the new x and y tile positions (x changed)
          newMap(row).append(f)
        }
      }
      castle.map = newMap
      true
    } else {
      false
    }
  }

  def expandCastleRight(castle: Castle, amount: Int): Boolean = {
    val curRows: Int = castle.map.size
    val curCols: Int = castle.map(0).size
    val newCols: Int = curCols + amount
    if (newCols <= 300) {
      val newMap = new ArrayBuffer[ArrayBuffer[Floor]]
      val curMap = castle.map
      for (row <- 0 until curRows) {
        newMap.append(new ArrayBuffer[Floor])
        for (col <- 0 until curCols) { //add current columns
          val f: Floor = curMap(row)(col) //no shift
          //no XY updated needed because the current tiles did not shift
          newMap(row).append(f)
        }
        for (col <- curCols until newCols) { //new cols added after current columns
          newMap(row).append(new Floor(None, col, row))
        }
      }
      castle.map = newMap
      true
    } else {
      false
    }
  }

  def expandCastleTop(castle: Castle, amount: Int): Boolean = {
    val curRows: Int = castle.map.size
    val curCols: Int = castle.map(0).size
    val newRows: Int = curRows + amount
    if (newRows <= 300) {
      val newMap = new ArrayBuffer[ArrayBuffer[Floor]]
      val curMap = castle.map
      for (row <- 0 until newRows) {
        newMap.append(new ArrayBuffer[Floor])
        if (row < amount) { //new rows that need to be added
          for (col <- 0 until curCols) {
            newMap(row).append(new Floor(None, col, row))
          }
        } else { //old rows
          for (col <- 0 until curCols) {
            val f: Floor = curMap((row - amount))(col) //rows shifted columns did not
            f.setXY(col, row) //set the new x and y tile positions (y changed)
            newMap(row).append(f)
          }
        }
      }
      castle.map = newMap
      true
    } else {
      false
    }
  }

  def expandCastleBottom(castle: Castle, amount: Int): Boolean = {
    val curRows: Int = castle.map.size
    val curCols: Int = castle.map(0).size
    val newRows: Int = curRows + amount
    if (newRows <= 300) {
      val newMap = new ArrayBuffer[ArrayBuffer[Floor]]
      val curMap = castle.map
      for (row <- 0 until newRows) {
        newMap.append(new ArrayBuffer[Floor])
        if (row >= curRows) { //new rows that need to be added to the bottom
          for (col <- 0 until curCols) {
            newMap(row).append(new Floor(None, col, row))
          }
        } else { //old rows
          for (col <- 0 until curCols) {
            val f: Floor = curMap(row)(col)
            //no XY updated needed because the current tiles did not shift
            newMap(row).append(f)
          }
        }
      }
      castle.map = newMap
      true
    } else {
      false
    }
  }
}

