package org.nolat.castleforge.castle

import scala.collection.mutable.ArrayBuffer

object ItemFactory {
  def createItem(itemType: String, params: List[String]): Item = {
    itemType match {
      case "checkpoint" => null //new Checkpoint(params)
      case "crystal_ball" => null
      case "door" => null
      case "endpoint" => null
      case _ => null
    }
  }
}