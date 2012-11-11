package org.nolat.castleforge.castle.items

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.PlayerListener
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.Player

object Item {
  def apply(itemType: String, _params: List[String]): Option[Item] = {
    val params = _params.map(param => if (param == null) "" else param)
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
    itemType match {
      case "crystal_ball" => Some(new CrystalBall())
      case "ice" => Some(new Ice)
      case "wall" => Some(new Wall)
      //the rest are defaults for the ones that usually require params
      case "checkpoint" => Some(new CheckPoint(false))
      case "door" => Some(new Door(0, "", ""))
      case "endpoint" => Some(new EndPoint("The End"))
      case "key" => Some(new Key("white", "diamond", 1))
      case "match" => Some(new Match(1))
      case "obstacle" => Some(new Obstacle("hole"))
      case "pusher" => Some(new Pusher("up"))
      case "sign" => Some(new Sign("Hello"))
      case "spawnpoint" => Some(new SpawnPoint(true))
      case "teleporter" => Some(new Teleporter("bidirectional", "blue"))
      case "torch" => Some(new Torch(false, "high", "white"))
      case _ => None
    }
  }

  val itemTypes = List(
    "checkpoint", "door", "endpoint", "ice", "obstacle", "pusher",
    "sign", "spawnpoint", "teleporter", "torch")

  val collectables = List("key", "match", "crystal_ball")
}

abstract class Item extends GameItem with PlayerListener {
  def getItemType: String = "n/a"
  def getParamList: Seq[String] = Nil
  var isBlockingMovement: Boolean = false
  var container: Floor = null

  def getOptions = List("")
}