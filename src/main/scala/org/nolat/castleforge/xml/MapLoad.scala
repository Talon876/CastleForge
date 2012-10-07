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
import org.nolat.castleforge.castle.Tile
import org.nolat.castleforge.castle.Inventory
import org.nolat.castleforge.castle.{Castle => CastleStructure}
import org.nolat.castleforge.castle.{Item => CastleItem}

object MapLoad {
  def loadMap(file: File, isEditor: Boolean): CastleStructure = {
    loadMap(file, Config.mapXsd, isEditor)
  }

  def loadMap(file: File, schema: InputStream, isEditor: Boolean): CastleStructure = {
    val source = FileUtils.openInputStream(file)
    loadMap(source, schema, isEditor)
  }

  def loadMap(stream: InputStream, xsdStream: InputStream, isEditor: Boolean): CastleStructure = {
    val factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
    val schema = factory.newSchema(new StreamSource(xsdStream))

    val xml = new SchemaAwareFactoryAdapter(schema).load(stream)
    var xmlCastle : Castle = null
    try
    {
    	xmlCastle = scalaxb.fromXML[Castle](xml)
    	createCastle(xmlCastle, isEditor)
    }
    catch
    {
      case e: Exception => println(e.getMessage() + e.getStackTrace().map(ex => println(ex.toString())))
      throw new Exception()
    }
  }

  private def createCastle(castleXML: Castle, isEditor: Boolean): CastleStructure = {
    val castle: CastleStructure = new CastleStructure(mapType2ABTile(castleXML.state(0).map))
    castle.name = castleXML.meta.name;
    castle.authorName = castleXML.meta.author;
    castle.rows = castleXML.roomlayout.rows.intValue
    castle.cols = castleXML.roomlayout.cols.intValue
    castle.roomLayout = seqStr2ABInt(castleXML.roomlayout.row)
    if(isEditor) //the editor should always get the original layout of the castle
    {
    	castle.map = castle.originalState
    	castle.inventory = itemType2Inventory(castleXML.state(0).inventory.orNull) //it will load the inventory of the original state
    }
    else
    {
	    if(castleXML.state.length == 1)
	    {
		    castle.map = castle.originalState
		    castle.inventory = itemType2Inventory(castleXML.state(0).inventory.orNull)
	    }
	    else
	    {
		    castle.map = mapType2ABTile(castleXML.state(1).map)
		    castle.inventory = itemType2Inventory(castleXML.state(1).inventory.orNull)
	    }
    }
    castle
  }

  private def seqStr2ABInt(str: Seq[String]): ArrayBuffer[ArrayBuffer[Int]] = {
    ArrayBuffer(str.map(s => ArrayBuffer(s.map(c =>  (c - '0')): _*)): _*) //http://stackoverflow.com/questions/6238353/scala-int-value-of-string-characters#comment7272535_6238411

  }

  private def mapType2ABTile(map: MapType): ArrayBuffer[ArrayBuffer[Tile]] = {
    ArrayBuffer(map.row.map(r => ArrayBuffer(r.tile.map(t => itemType2Tile(t)): _*) ): _* )
  }
  
  private def itemType2Tile(t :CastleForgeItemType) : Tile ={
    if(t.item != null && t.item.length > 0)
    {
    	new Tile(item2Item(t.item(0)))
    }
    else
      new Tile()
  }
  private def item2Item(i : Item) :CastleItem = {
    new CastleItem(i.typeValue,ArrayBuffer(i.param : _*))
  }
  private def itemType2Inventory(inven : CastleForgeItemType) : Inventory =
  {
    if(inven != null)
    {
      return new Inventory(itemType2Items(inven))
    }
    else
    {
      return new Inventory()
    }
  }
  private def itemType2Items(itemType: CastleForgeItemType) : Seq[CastleItem] =
  {
    itemType.item.map(i => item2Item(i))
  }
}