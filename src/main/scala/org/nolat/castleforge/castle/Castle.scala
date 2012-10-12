package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Castle(origState : ArrayBuffer[ArrayBuffer[Floor]]) {
	var name:String = "Default"
	var authorName: String = "Default Name"
	var description: String = ""
	var roomLayout: ArrayBuffer[ArrayBuffer[Int]] = new ArrayBuffer()
	var rows: Int = org.nolat.castleforge.Config.DefaultCastleSize._1
	var cols: Int = org.nolat.castleforge.Config.DefaultCastleSize._2
	
	val originalState: ArrayBuffer[ArrayBuffer[Floor]] = origState
	var map: ArrayBuffer[ArrayBuffer[Floor]] = new ArrayBuffer()
	var inventory: Inventory = new Inventory()
	
	def this(origState : ArrayBuffer[ArrayBuffer[Floor]], nam:String, authorNam:String, descript:String) {
		this(origState)
		name= nam
		authorName = authorNam
		description = descript
	}
}