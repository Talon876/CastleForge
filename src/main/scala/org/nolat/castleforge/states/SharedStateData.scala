package org.nolat.castleforge.states

import java.io.File
import org.nolat.castleforge.xml.MapLoad
import org.nolat.castleforge.castle.Castle

object SharedStateData {
    private var _loadedCastle : Castle = null
    def loadedCastle = _loadedCastle
	def loadedCastle_=(t: (File,Boolean)) = {
      mapFile = t._1
      if(mapFile != null)
      {
    	  _loadedCastle  = MapLoad.loadMap(mapFile, t._2)
      }
      else
      {
    	  _loadedCastle = null
      }
	}
	private var _mapFile : File = null
	def mapFile = _mapFile
	def mapFile_= (map: File) = {
	  _mapFile = map
	}
	
}