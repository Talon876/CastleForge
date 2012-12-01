package org.nolat.castleforge.states

import java.io.File
import org.nolat.castleforge.xml.MapLoad
import org.nolat.castleforge.castle.Castle

object SharedStateData {
  private var _loadedCastle: Castle = null

  def loadedCastle = _loadedCastle

  def loadedCastle_=(mapFile: File) = {
    if (mapFile != null) {
      _loadedCastle = MapLoad.loadMap(mapFile, loadOriginal)
    } else {
      _loadedCastle = null
    }
  }

  def loadedCastle_=(castle: Castle) {
    _loadedCastle = castle
  }

  private var _mapFile: File = null
  def mapFile = _mapFile
  def mapFile_=(map: File) = {
    _mapFile = map
  }
  var loadOriginal = false //If true it will always load state 0 if false it will load state 1 if it exists or state 0
}