package org.nolat.castleforge.castle.items
object Item {
  def apply(itemType: String, params: List[String]): Option[Item] = { //find better way to handle null/None
    itemType match {
      case "checkpoint" => None
      case "crystal_ball" => None
      case "door" => None
      case "endpoint" => None
      case "floor" => None
      case "key" => None
      case "match" => None
      case "obstacle" => None
      case "pusher" => None
      case "sign" => None
      case "spawnpoint" => None
      case "teleporter" => None
      case "torch" => None
      case "wall" => None
      case _ => None
    }
  }
  def apply(itemType: String): Option[Item] = {
    itemType match {
      case "ice" => Some(new Ice())
      case _ => None
    }
  }
}

abstract class Item extends GameItem {

  def getItemType: String = "n/a"
  def getParamList: Seq[String] = Nil
  //TODO create attributes

}