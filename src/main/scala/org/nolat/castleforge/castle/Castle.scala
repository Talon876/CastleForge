package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Castle {
	var name:String = "Default"
	var authorName: String = "Default Name"
	var description: String = ""
	var roomLayout: ArrayBuffer[ArrayBuffer[Int]] = null
	var rows: Int = 0
	var cols: Int = 0
	
	var map: ArrayBuffer[ArrayBuffer[Tile]] = null
	var inventory: Inventory = null
	
	def this(nam:String, authorNam:String, descript:String) {
		this()
		name= nam
		authorName = authorNam
		description = descript
	}
}