package org.nolat.castleforge.castle

class Tile {
	var item : Item = null;
	
	def this(tile: org.nolat.castleforge.xml.CastleForgeItemType)
	{
	  this()
	  item = new Item(tile.item(0))
	}
	
	//Implicit conversion to inventory if necessary later
}