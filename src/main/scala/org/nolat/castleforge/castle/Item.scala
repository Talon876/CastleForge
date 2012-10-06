package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

class Item {
	var itemType : String = ""
	var params : ArrayBuffer[String] = null
	
	def this(item: org.nolat.castleforge.xml.Item)
	{
	  this()
	  itemType = item.typeValue;
	  params = ArrayBuffer(item.param : _*)
	}
}