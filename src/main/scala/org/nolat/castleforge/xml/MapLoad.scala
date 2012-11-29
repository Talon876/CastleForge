package org.nolat.castleforge.xml

import javax.xml.validation.SchemaFactory
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import java.io.File
import org.apache.commons.io.FileUtils
import java.io.InputStream
import org.nolat.castleforge.Config
import scala.collection.mutable.MutableList
import collection.breakOut
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Inventory
import org.nolat.castleforge.castle.{ Castle => CastleStructure }
import org.nolat.castleforge.castle.items.{ Item => CastleItem }
import org.nolat.castleforge.castle.items.attributes.Collectable

object MapLoad {

  def loadMap(file: File, isEditor: Boolean): CastleStructure = {
    loadMap(file, Config.mapXsd, isEditor)
  }

  def loadMap(file: File, schema: InputStream, loadOriginal: Boolean): CastleStructure = {
    val xmlCastle = getXMLMap(file, schema)
    createCastle(xmlCastle, file, loadOriginal)
  }
  
  def getXMLMap(file: File, xsdStream: InputStream):Castle ={
    val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val schema = factory.newSchema(new StreamSource(xsdStream))
    val stream = FileUtils.openInputStream(file)
    val xml = new SchemaAwareFactoryAdapter(schema).load(stream)
    var xmlCastle: Castle = null
    try {
      xmlCastle = scalaxb.fromXML[Castle](xml)
      
    } catch {
      case e: Exception =>
        println(e.getMessage() + e.getStackTrace().map(ex => println(ex.toString())))
        throw new Exception()
    }
    xmlCastle
  }
  private def createCastle(castleXML: Castle, castleFile : File, loadOriginal: Boolean): CastleStructure = {
    var castle: CastleStructure = null //loads the 0 state as the original map so you can reset progress to default
    var tiles: ArrayBuffer[ArrayBuffer[Floor]] = null
    var inventory: Inventory = null
    if (loadOriginal) //the editor should always get the original layout of the castle
    {
      tiles = mapType2ABTile(castleXML.state(0).map)
      inventory = itemType2Inventory(castleXML.state(0).inventory) //it will load the inventory of the original state
    } else {//Load original if there is no checkpointstate
      if (castleXML.state.length == 1) {
        tiles = mapType2ABTile(castleXML.state(0).map)
        inventory = itemType2Inventory(castleXML.state(0).inventory)
      } else {
        tiles = mapType2ABTile(castleXML.state(1).map)
        inventory = itemType2Inventory(castleXML.state(1).inventory)
      }
    }
    castle = CastleStructure(tiles, castleXML.meta.name, castleXML.meta.author, castleXML.meta.description)
    castle.inventory = inventory
    castle
  }

  private def seqStr2ABInt(str: Seq[String]): ArrayBuffer[ArrayBuffer[Int]] = {
    ArrayBuffer(str.map(s => ArrayBuffer(s.map(c => (c - '0')): _*)): _*) //http://stackoverflow.com/questions/6238353/scala-int-value-of-string-characters#comment7272535_6238411

  }

  private def mapType2ABTile(map: MapType): ArrayBuffer[ArrayBuffer[Floor]] = {

    val castle = map.row.zipWithIndex.map {
      case (row, y) => row.tile.zipWithIndex.map {
        case (tyle, x) => {
          itemType2Floor(tyle, x, y)
        }
      }
    }

    //create arraybuffer of each row and each row is a seq of floor
    val buffers = castle.map { row =>
      ArrayBuffer(row: _*)
    }
    //create array buffer of the seq of rows
    val result = ArrayBuffer(buffers: _*)
    result
  }

  private def itemType2Floor(t: CastleForgeTileType, x: Int, y: Int): Floor = {
    val tileitem: Option[CastleItem] = tile2CastleItem(t) //The floor tiles only use the first item in the "inventory"
    val roomIds: String = t.roomids
    new Floor(tileitem, x, y, roomIds)
  }

  private def tile2CastleItem(t: CastleForgeTileType): Option[CastleItem] = {
    t.item match {
      case Some(itm) => {
        itm.param.size > 0 match {
          case true => CastleItem(itm.typeValue, itm.param.toList)
          case false => CastleItem(itm.typeValue)
        }
      }
      case None => None
    }

  }
  private def itemType2Inventory(inven: Option[CastleForgeItemType]): Inventory = {
    inven match {
      case Some(inv) => new Inventory(itemType2Collectables(inv))
      case None => new Inventory()
    }
  }

  private def itemType2Collectables(itemType: CastleForgeItemType): Seq[Option[Collectable]] = {
    itemType.invItem.isEmpty match {
      case false => {
        itemType.invItem.map(i => item2Collectable(i))
      }
      case true => Seq(None)
    }
  }
  private def item2Collectable(i: InvItem): Option[Collectable] = {

    val item = i.param.isEmpty match {
      case false => CastleItem(i.typeValue, i.param.toList)
      case true => CastleItem(i.typeValue)
    }
    convertToCollectable(item)
  }

  private def convertToCollectable(item: Option[CastleItem]): Option[Collectable] = {
    val isCollectable = item match {
      case Some(itm) => itm match {
        case i : Collectable => true //isInstanceOf does not check traits that are mixed in http://stackoverflow.com/questions/2809463/how-to-get-list-of-traits-that-were-mixed-in-the-specified-class
        case _ => false
      }
      case None => false
    }
    if (isCollectable) Some(item.get.asInstanceOf[Collectable]) else None
  }
}