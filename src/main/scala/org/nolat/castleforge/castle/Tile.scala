package org.nolat.castleforge.castle

class Tile {
	var item : Item = new Item();
	
	def this(tile: Item)
	{
	  this()
	  item = item
	}
	
	//Implicit conversion to inventory if necessary later
}