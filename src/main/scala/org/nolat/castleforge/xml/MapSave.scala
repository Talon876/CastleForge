package org.nolat.castleforge.xml

import java.io.File
import scala.Option.option2Iterable
import scala.annotation.implicitNotFound
import scala.collection.mutable.ArrayBuffer
import scala.math.BigDecimal.int2bigDecimal
import scala.math.BigInt.int2bigInt
import scala.xml.XML
import org.nolat.castleforge.castle.{ Castle => CastleStructure }
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Inventory
import org.nolat.castleforge.castle.items.{ Item => CastleItem }
import org.nolat.castleforge.Config
import com.rits.cloning.Cloner
import com.rits.cloning.IFastCloner
import org.nolat.castleforge.castle.items._
import scala.actors.Future
import scala.actors.Futures.future
import org.nolat.castleforge.tools.CodeTimer

object MapSave {
  var saveDone: Future[Boolean] = future {
    false //not done, dummy future incase save has not ever been called
  }
  val cloner: Cloner = new Cloner();
  //cloner.setDumpClonedClasses(true);
  setupCloner()

  def dummyInit() { //does nothing but allows MapSave to be initalized
  }
  private def save(castle: Castle, saveFile: File) =
    {
      val xml = scalaxb.toXML[Castle](castle, None, Some("castle"), defaultScope)
      XML.save(saveFile.getAbsolutePath(), xml(0), "UTF-8", true, null)
    }

  private def saveCastle(castle: CastleStructure, state: Seq[State], saveFile: File) {
    val meta = new Meta(castle.name, castle.authorName, castle.description)
    val version = 1
    val xmlCastle: Castle = new Castle(meta, state, version)
    save(xmlCastle, saveFile)
  }

  def save(castle: CastleStructure, isEditor: Boolean = false, saveFile: Option[File] = None) {
    //println("saving to " + castle.fileLocation.getAbsolutePath() + " (editor=" + isEditor + ")")
    var state: Seq[State] = Nil
    if (isEditor) {
      /*
	   * This is used when saving a castle in the castle editor
	   * The castle editor will be updating the same map that the game engine uses
	   * but is allowed more freedom to change the map state
	   * instead of saving the original map to state 0 it will save the updated map
	   * and will not write out a checkpoint state (1)
	   */
      state = List[State](new State(AB2State(castle.map), inv2ItemType(castle.inventory), 0))
      saveFile match {
        case None => {
          saveCastle(castle, state, castle.fileLocation)
        }
        case Some(f) => {
          saveCastle(castle, state, f)
        }
      }
    } else {
      println("Clone starting")
      CodeTimer.start()
      val map = cloneMap(castle.map)
      val inv = cloneInventory(castle.inventory)
      CodeTimer.finish()
      saveDone = future {
        println("Saving starting")
        CodeTimer.start()
        
        /*
	   * This is used when saving a checkpoint while playing through a castle
	   * It will save the players inventory and the current state of the map
	   * to state id 1 which is the checkpoint state
	   */
        if (castle.fileLocation == null) //all non editor castles should be loaded from files
        {
          return
        }
        val origState = MapUtils.getState(castle.fileLocation, 0)
        state = List[State](origState, new State(AB2State(map), inv2ItemType(inv), 1))
        saveFile match {
          case None => {
            saveCastle(castle, state, castle.fileLocation)
            CodeTimer.finish()
            println("Save finished")
            true //done
          }
          case Some(f) => {
            saveCastle(castle, state, f)
            CodeTimer.finish()
            println("Save finished")
            true //done
          }
        }
        
      }
      println("is not waiting on saving to be done")
    }
  }
  private def AB2Roomlayout(buffer: ArrayBuffer[ArrayBuffer[Int]]): Seq[String] = {
    buffer.map(row => new String(row.map(i => (i + '0').toChar).toArray))
  }

  private def AB2State(buffer: ArrayBuffer[ArrayBuffer[Floor]]): MapType = {
    new MapType(buffer.map(row => seq2Row(row)): _*)
  }

  private def seq2Row(seq: Seq[Floor]): Row = {
    new Row(seq.map(t => tile2CastleForgeTileType(t)): _*)
  }

  private def tile2CastleForgeTileType(tile: Floor): CastleForgeTileType = {
    tile.item match {
      //TODO: change tile.roomIDs to proper getter
      case Some(i) => new CastleForgeTileType(Some(item2Item(i)), tile.roomIDs) //if the tile has an item save it
      case None => new CastleForgeTileType(None, tile.roomIDs) //save with a blank "inventory"
    }
  }
  private def item2Item(item: CastleItem): Item = {
    new Item(item.getParamList, item.getItemType)

  }
  private def tile2CastleForgeItemType(tile: Floor): CastleForgeItemType = {
    tile.item match {
      case Some(i) => new CastleForgeItemType(item2InvItem(i)) //if the tile has an item save it
      case None => new CastleForgeItemType() //save with a blank "inventory"
    }
  }
  private def item2InvItem(item: CastleItem): InvItem = {
    new InvItem(item.getParamList, item.getItemType)

  }
  private def inv2ItemType(inv: Inventory): Option[CastleForgeItemType] = {
    val sequence = List(inv.flatten.map {
      item => item2InvItem(item)
    }: _*)

    var items: Option[CastleForgeItemType] = None
    if (sequence.length > 0) //not perfect scala but it needs to check if there are any actual items in the inventory
    {
      items = Some(new CastleForgeItemType(sequence: _*)) //save all valid items out
    }
    items
  }

  private def cloneMap(map: ArrayBuffer[ArrayBuffer[Floor]]): ArrayBuffer[ArrayBuffer[Floor]] = {
    val floors: ArrayBuffer[ArrayBuffer[Floor]] = new ArrayBuffer[ArrayBuffer[Floor]]
    map.foreach { floorRow =>
      val newRow = new ArrayBuffer[Floor]
      floorRow.foreach { floor =>
        newRow.append(cloner.deepClone(floor))
      }
      floors.append(newRow)
    }
    floors
  }

  private def cloneInventory(inv: Inventory): Inventory = {
    cloner.deepClone(inv)
  }

  private def setupCloner() {
    cloner.dontCloneInstanceOf(scala.collection.immutable.List.getClass())
    cloner.dontCloneInstanceOf(scala.collection.immutable.$colon$colon.getClass())
    cloner.dontCloneInstanceOf(classOf[org.nolat.castleforge.graphics.Sprite])
    cloner.dontCloneInstanceOf(classOf[org.newdawn.slick.geom.Vector2f])
    cloner.dontCloneInstanceOf(classOf[org.newdawn.slick.Color])

    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.Floor], new FastClonerFloors())
    cloner.registerFastCloner(
      classOf[org.nolat.castleforge.castle.Inventory], new FastClonerInventories())

    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.CheckPoint], new FastClonerCheckPoint())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Door], new FastClonerDoor())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.EndPoint], new FastClonerEndPoint())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Key], new FastClonerKey())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Match], new FastClonerMatch())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Wall], new FastClonerWall())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Pusher], new FastClonerPusher())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Sign], new FastClonerSign())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.SpawnPoint], new FastClonerSpawnPoint())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Teleporter], new FastClonerTeleporter())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Torch], new FastClonerTorch())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.CrystalBall], new FastClonerCrystalBall())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Ice], new FastClonerIce())
    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Obstacle], new FastClonerObstacle())

    cloner.registerFastCloner(classOf[org.nolat.castleforge.castle.items.Item], new FastClonerItems())
  }
}

class FastClonerItems extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: CastleItem = t.asInstanceOf[CastleItem]
    val itmN: CastleItem = CastleItem(itm.getItemType, itm.getParamList.toList).get
    itmN
  }
}

class FastClonerFloors extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val flr: Floor = t.asInstanceOf[Floor]
    val itm: Option[CastleItem] = flr.item match {
      case Some(i) => Some(cloner.cloneInternal(i, clones))
      case None => None
    }
    val flrN: Floor = new Floor(itm, flr.x, flr.y, flr.roomIDs)
    flrN
  }
}

class FastClonerInventories extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val inv: Inventory = t.asInstanceOf[Inventory]
    val invN: Inventory = new Inventory()
    inv.foreach { i =>
      i match {
        case Some(itm) => invN.addItem(cloner.cloneInternal(itm, clones))
        case None =>
      }
    }
    invN
  }
}

class FastClonerCheckPoint extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: CheckPoint = t.asInstanceOf[CheckPoint]
    val itmN: CheckPoint = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[CheckPoint]
    itmN
  }
}
class FastClonerDoor extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Door = t.asInstanceOf[Door]
    val itmN: Door = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Door]
    itmN
  }
}
class FastClonerEndPoint extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: EndPoint = t.asInstanceOf[EndPoint]
    val itmN: EndPoint = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[EndPoint]
    itmN
  }
}
class FastClonerKey extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Key = t.asInstanceOf[Key]
    val itmN: Key = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Key]
    itmN
  }
}
class FastClonerMatch extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Match = t.asInstanceOf[Match]
    val itmN: Match = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Match]
    itmN
  }
}
class FastClonerWall extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Wall = t.asInstanceOf[Wall]
    val itmN: Wall = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Wall]
    itmN
  }
}
class FastClonerObstacle extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Obstacle = t.asInstanceOf[Obstacle]
    val itmN: Obstacle = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Obstacle]
    itmN
  }
}
class FastClonerPusher extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Pusher = t.asInstanceOf[Pusher]
    val itmN: Pusher = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Pusher]
    itmN
  }
}
class FastClonerSign extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Sign = t.asInstanceOf[Sign]
    val itmN: Sign = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Sign]
    itmN
  }
}
class FastClonerSpawnPoint extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: SpawnPoint = t.asInstanceOf[SpawnPoint]
    val itmN: SpawnPoint = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[SpawnPoint]
    itmN
  }
}
class FastClonerTeleporter extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Teleporter = t.asInstanceOf[Teleporter]
    val itmN: Teleporter = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Teleporter]
    itmN
  }
}

class FastClonerTorch extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Torch = t.asInstanceOf[Torch]
    val itmN: Torch = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Torch]
    itmN
  }
}
class FastClonerCrystalBall extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: CrystalBall = t.asInstanceOf[CrystalBall]
    val itmN: CrystalBall = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[CrystalBall]
    itmN
  }
}
class FastClonerIce extends IFastCloner {
  def clone(t: java.lang.Object, cloner: Cloner, clones: java.util.Map[java.lang.Object, java.lang.Object]): java.lang.Object = {
    val itm: Ice = t.asInstanceOf[Ice]
    val itmN: Ice = CastleItem(itm.getItemType, itm.getParamList.toList).get.asInstanceOf[Ice]
    itmN
  }
}