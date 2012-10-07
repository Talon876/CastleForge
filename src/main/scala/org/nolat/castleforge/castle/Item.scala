package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Item {
	var itemType : String = ""
	var params : ArrayBuffer[String] = new ArrayBuffer
	
	def this(iType : String, param : ArrayBuffer[String])
	{
	  this()
	  itemType = iType
	  params = param
	}
}