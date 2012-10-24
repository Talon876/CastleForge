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

object MapLoad {

  def loadMap(file: File, isEditor: Boolean): CastleStructure = {
    loadMap(file, Config.mapXsd, isEditor)
  }

  def loadMap(file: File, schema: InputStream, isEditor: Boolean): CastleStructure = {
    val source = FileUtils.openInputStream(file)
    loadMap(source, schema, isEditor)
  }
  def loadMap(stream: InputStream, isEditor: Boolean): CastleStructure = {
    loadMap(stream, Config.mapXsd, isEditor)
  }
  def loadMap(stream: InputStream, xsdStream: InputStream, isEditor: Boolean): CastleStructure = {
    val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val schema = factory.newSchema(new StreamSource(xsdStream))

    val xml = new SchemaAwareFactoryAdapter(schema).load(stream)
    var xmlCastle: Castle = null
    try {
      xmlCastle = scalaxb.fromXML[Castle](xml)
      createCastle(xmlCastle, isEditor)
    } catch {
      case e: Exception =>
        println(e.getMessage() + e.getStackTrace().map(ex => println(ex.toString())))
        throw new Exception()
    }
  }

  private def createCastle(castleXML: Castle, isEditor: Boolean): CastleStructure = {
    val castle: CastleStructure = new CastleStructure(mapType2ABTile(castleXML.state(0).map)) //loads the 0 state as the original map so you can reset progress to default
    //to support an initial inventory you would want to to add it to the castle currently the inventory will reset to blank
    castle.name = castleXML.meta.name;
    castle.authorName = castleXML.meta.author;
    castle.rows = castleXML.roomlayout.rows.intValue
    castle.cols = castleXML.roomlayout.cols.intValue
    castle.roomLayout = seqStr2ABInt(castleXML.roomlayout.row)
    if (isEditor) //the editor should always get the original layout of the castle
    {
      castle.map = mapType2ABTile(castleXML.state(0).map)
      castle.inventory = itemType2Inventory(castleXML.state(0).inventory) //it will load the inventory of the original state
    } else {
      if (castleXML.state.length == 1) {
        castle.map = mapType2ABTile(castleXML.state(0).map)
        castle.inventory = itemType2Inventory(castleXML.state(0).inventory)
      } else {
        castle.map = mapType2ABTile(castleXML.state(1).map)
        castle.inventory = itemType2Inventory(castleXML.state(1).inventory)
      }
    }
    castle
  }

  private def seqStr2ABInt(str: Seq[String]): ArrayBuffer[ArrayBuffer[Int]] = {
    ArrayBuffer(str.map(s => ArrayBuffer(s.map(c => (c - '0')): _*)): _*) //http://stackoverflow.com/questions/6238353/scala-int-value-of-string-characters#comment7272535_6238411

  }

  private def mapType2ABTile(map: MapType): ArrayBuffer[ArrayBuffer[Floor]] = {

    val castle = map.row.zipWithIndex.map {
      case (row, y) => row.tile.zipWithIndex.map {
        case (tyle, x) => {
          itemType2Tile(tyle, x, y)
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

  private def itemType2Tile(t: CastleForgeItemType, x: Int, y: Int): Floor = {
    val tileitem: Option[CastleItem] = itemType2Items(t)(0) //The floor tiles only use the first item in the "inventory"

    new Floor(tileitem, x, y)
  }
  private def item2Item(i: Item): Option[CastleItem] = {
    i.param.isEmpty match {
      case false => CastleItem(i.typeValue, i.param.toList)
      case true => CastleItem(i.typeValue)
    }

  }
  private def itemType2Inventory(inven: Option[CastleForgeItemType]): Inventory = {
    inven match {
      case Some(inv) => new Inventory(itemType2Items(inv))
      case None => new Inventory()
    }
  }
  private def itemType2Items(itemType: CastleForgeItemType): Seq[Option[CastleItem]] = {
    itemType.item.isEmpty match {
      case false => itemType.item.map(i => item2Item(i))
      case true => Seq(None)
    }
  }
}