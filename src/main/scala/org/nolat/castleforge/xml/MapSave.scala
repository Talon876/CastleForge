package org.nolat.castleforge.xml

import java.io.File
import scala.xml.XML

object MapSave {
  def save(castle: Castle, savePath: String) =
    {
      val xml = scalaxb.toXML[Castle](castle, None, Some("castle"), defaultScope)
      val mapsFolder = new File(savePath)
      mapsFolder.mkdirs()
      xml.foreach(node => XML.save(savePath + "/" + castle.meta.author + "-" + castle.meta.name + ".xml", node, "UTF-8", true, null))
    }
}