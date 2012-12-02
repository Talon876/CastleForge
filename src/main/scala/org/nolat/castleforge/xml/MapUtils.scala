package org.nolat.castleforge.xml

import java.io.File
import org.apache.commons.io.FileUtils
import org.nolat.castleforge.Config

object MapUtils {
  def getState(file: File, stateID: Int): State = {
    if (stateID < 0)
      null
    val map = MapLoad.getXMLMap(file, Config.mapXsd)
    if (map.state.size >= (stateID + 1)) {
      map.state(stateID)
    } else {
      null
    }
  }
  /** 
   * Only needs to load the file to get the metadata structure from it
   */
  def getMeta(file: File): Option[Meta] = {
    try
    {
      val map = MapLoad.getXMLMap(file, Config.mapXsd) 
      Some(map.meta)
    }
    catch{case _ => None}
  }
}