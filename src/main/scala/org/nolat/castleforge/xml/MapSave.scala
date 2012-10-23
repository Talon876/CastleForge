package org.nolat.castleforge.xml

import java.io.File
import scala.xml.XML
import org.nolat.castleforge.castle.{ Castle => CastleStructure }
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Inventory
import scala.collection.mutable.ArrayBuffer
import org.nolat.castleforge.castle.items.{ Item => CastleItem }

object MapSave {
  private def save(castle: Castle, saveDirectory: String) =
    {
      val xml = scalaxb.toXML[Castle](castle, None, Some("castle"), defaultScope)
      val mapsFolder = new File(saveDirectory)
      mapsFolder.mkdirs()
      XML.save(saveDirectory + "/" + castle.meta.author + "-" + castle.meta.name + ".xml", xml(0), "UTF-8", true, null)
    }
  private def saveCastle(castle: CastleStructure, savePath: String, state: Seq[State]) {
    val meta = new Meta(castle.name, castle.authorName, castle.description)
    val roomlayout = new Roomlayout(AB2Roomlayout(castle.roomLayout), castle.rows, castle.cols)
    val version = 1
    val xmlCastle: Castle = new Castle(meta, roomlayout, state, version)
    save(xmlCastle, savePath)
  }

  def save(castle: CastleStructure, savePath: String, isEditor: Boolean) {
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
      saveCastle(castle, savePath, state) //TODO: change to using online database later
    } else {
      /*
	   * This is used when saving a checkpoint while playing through a castle
	   * It will save the players inventory and the current state of the map
	   * to state id 1 which is the checkpoint state
	   */
      state = List[State](new State(AB2State(castle.originalState), None, 0), new State(AB2State(castle.map), inv2ItemType(castle.inventory), 1))
      saveCastle(castle, savePath, state)
    }

  }
  private def AB2Roomlayout(buffer: ArrayBuffer[ArrayBuffer[Int]]): Seq[String] = {
    buffer.map(row => new String(row.map(i => (i + '0').toChar).toArray))
  }

  private def AB2State(buffer: ArrayBuffer[ArrayBuffer[Floor]]): MapType = {
    new MapType(buffer.map(row => seq2Row(row)): _*)
  }

  private def seq2Row(seq: Seq[Floor]): Row = {
    new Row(seq.map(t => tile2CastleForgeItemType(t)): _*)
  }
  private def tile2CastleForgeItemType(tile: Floor): CastleForgeItemType = {
    tile.item match { 
      case Some(i) => new CastleForgeItemType(item2Item(i)) //if the tile has an item save it
      case None => new CastleForgeItemType() //save with a blank "inventory"
    }
  }
  private def item2Item(item: CastleItem): Item = {
    new Item(item.getParamList, item.getItemType)

  }
  private def inv2ItemType(inv: Inventory): Option[CastleForgeItemType] = {
    val sequence = List(inv.flatten.map {
      item => item2Item(item)
    }: _*)
    
    var items: Option[CastleForgeItemType] = None
    if (sequence.length > 0) //not perfect scala but it needs to check if there are any actual items in the inventory
    {
      items = Some(new CastleForgeItemType(sequence: _*)) //save all valid items out
    }
    items
  }
}