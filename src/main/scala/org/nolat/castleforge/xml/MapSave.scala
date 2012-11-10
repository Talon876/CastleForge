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

object MapSave {
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

  def save(castle: CastleStructure, isEditor: Boolean = false, saveFile: Option[File] = None): File = {
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
    } else {
      /*
	   * This is used when saving a checkpoint while playing through a castle
	   * It will save the players inventory and the current state of the map
	   * to state id 1 which is the checkpoint state
	   */
      if (castle.fileLocation == null) //all non editor castles should be loaded from files
      {
        return null
      }
      val origState = MapUtils.getState(castle.fileLocation, 0)
      state = List[State](origState, new State(AB2State(castle.map), inv2ItemType(castle.inventory), 1))
    }
    saveFile match {
      case None => {
        saveCastle(castle, state, castle.fileLocation)
        castle.fileLocation
      }
      case Some(f) => {
        saveCastle(castle, state, f)
        f
      }
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
}