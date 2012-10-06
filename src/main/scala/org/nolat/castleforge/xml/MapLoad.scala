package org.nolat.castleforge.xml

import javax.xml.validation.SchemaFactory
import javax.xml.XMLConstants
import javax.xml.transform.stream.StreamSource
import java.io.File
import org.apache.commons.io.FileUtils
import java.io.InputStream
import org.nolat.castleforge.Config
import org.nolat.castleforge.castle.{ Castle => CastleStructure }
import scala.collection.mutable.MutableList
import collection.breakOut
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.Buffer
import org.nolat.castleforge.castle.Tile
import org.nolat.castleforge.castle.Inventory

object MapLoad {
  def loadMap(file: File): Castle = {
    loadMap(file, Config.mapXsd)
  }

  def loadMap(file: File, schema: InputStream): Castle = {
    val source = FileUtils.openInputStream(file)
    loadMap(source, schema)
  }

  def loadMap(stream: InputStream, xsdStream: InputStream): Castle = {
    val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val schema = factory.newSchema(new StreamSource(xsdStream))

    val xml = new SchemaAwareFactoryAdapter(schema).load(stream)

    scalaxb.fromXML[Castle](xml)
  }

  def createCastle(castleXML: Castle): CastleStructure = {
    val castle: CastleStructure = new CastleStructure
    castle.name = castleXML.meta.name;
    castle.authorName = castleXML.meta.author;
    castle.rows = castleXML.roomlayout.rows.asInstanceOf[Int]
    castle.cols = castleXML.roomlayout.cols.asInstanceOf[Int]
    castle.roomLayout = seqStr2ABInt(castleXML.roomlayout.row)
    if(castleXML.state.length == 1)
    {
	    castle.map = mapType2ABTile(castleXML.state(0).map)
	    castle.inventory = itemType2Inventory(castleXML.state(0).inventory.orNull)
    }
    else
    {
	    castle.map = mapType2ABTile(castleXML.state(1).map)
	    castle.inventory = itemType2Inventory(castleXML.state(1).inventory.orNull)
    }
    castle
  }

  def seqStr2ABInt(str: Seq[String]): ArrayBuffer[ArrayBuffer[Int]] = {
    ArrayBuffer(str.map(s => ArrayBuffer(s.map(c => c.toInt): _*)): _*)

  }

  def mapType2ABTile(map: MapType): ArrayBuffer[ArrayBuffer[Tile]] = {
    ArrayBuffer(map.row.map(r => ArrayBuffer(r.tile.map(t => new Tile(t)): _*) ): _* )
  }
  
  def itemType2Inventory(inven : CastleForgeItemType) : Inventory =
  {
    if(inven != null)
    {
      return new Inventory(inven)
    }
    else
    {
      return new Inventory()
    }
  }
}