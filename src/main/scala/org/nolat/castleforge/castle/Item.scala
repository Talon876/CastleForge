package org.nolat.castleforge.castle

abstract class Item {

  def getItemType: String = "n/a"
  def getParamList: Seq[String] = Nil
}