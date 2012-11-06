package org.nolat.castleforge.castle.items


abstract class Collectable extends Item {
  def equalCollectable(a: Collectable): Boolean
}

object Collectable {
  def apply(itemType: String, _params: List[String]): Option[Collectable] = {
    val params = _params.map(param => if (param == null) "" else param)
    itemType match {
      case "key" => Some(new Key(params))
      case "match" => Some(new Match(params))
      case _ => None
    }
  }
  def apply(itemType: String): Option[Collectable] = {
    itemType match {
      case "crystal_ball" => Some(new CrystalBall())
      case _ => None
    }
  }
}