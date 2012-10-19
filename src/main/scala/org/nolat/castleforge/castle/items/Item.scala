package org.nolat.castleforge.castle.items

object Item {
  def apply(itemType: String, _params: List[String]): Option[Item] = {
    val params = _params.map(param => if (param == null) "" else param)
    println("creating " + itemType + " with " + params)
    itemType match {
      case "checkpoint" => Some(new CheckPoint(params))
      case "door" => Some(new Door(params))
      case "endpoint" => Some(new EndPoint(params))
      case "key" => Some(new Key(params))
      case "match" => Some(new Match(params))
      case "obstacle" => Some(new Obstacle(params))
      case "pusher" => Some(new Pusher(params))
      case "sign" => Some(new Sign(params))
      case "spawnpoint" => Some(new SpawnPoint(params))
      case "teleporter" => Some(new Teleporter(params))
      case "torch" => Some(new Torch(params))
      case _ => None
    }
  }
  def apply(itemType: String): Option[Item] = {
    println("creating " + itemType)
    itemType match {
      case "crystal_ball" => Some(new CrystalBall())
      case "ice" => Some(new Ice)
      case "wall" => Some(new Wall)
      case _ => None
    }
  }
}

abstract class Item extends GameItem {
  def getItemType: String = "n/a"
  def getParamList: Seq[String] = Nil

}