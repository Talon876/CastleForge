package org.nolat.castleforge.states

import java.io.File
import org.nolat.castleforge.xml.MapLoad
import org.nolat.castleforge.castle.Castle

object SharedStateData {
  private var _loadedCastle: Castle = null

  private var _screenToLoad: Int = GameScreen.ID

  def screenToLoad = { //screenToLoad defaults to GameScreen and will reset to GameScreen needs to be set to load the Editor
    var temp = _screenToLoad
    _screenToLoad = GameScreen.ID
    temp
  }

  def screenToLoad_=(screen: Int) {
    _screenToLoad = screen
  }

  def loadedCastle = {
    var temp = _loadedCastle
    _loadedCastle = null
    temp
  }

  def loadedCastle_=(mapFile: File) = {
    if (mapFile != null) {
      _loadedCastle = MapLoad.loadMap(mapFile, loadOriginal)
      _mapFile = null
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

  var winString = ""
}