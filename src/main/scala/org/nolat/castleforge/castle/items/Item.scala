package org.nolat.castleforge.castle.items

import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics

object Item {
  def apply(itemType: String, _params: List[String]): Option[Item] = {
    val params = _params.map(param => if (param == null) "" else param)
    itemType match {

      case "checkpoint" => Some(new CheckPoint(params))
      case "door" => println(params); Some(new Door(params))
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
      case _ => None
    }
  }
}

abstract class Item extends GameItem {
  def getItemType: String = "n/a"
  def getParamList: Seq[String] = Nil

  def render(x: Int, y: Int, container: GameContainer, game: StateBasedGame, g: Graphics) {
    this.sprite.getAnimation.draw(x, y, this.color)
  }
}