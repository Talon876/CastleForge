package org.nolat.castleforge.castle

import org.nolat.castleforge.castle.items.Torch
import org.nolat.castleforge.castle.items.Wall

class Lighting(castle: Castle) {

  def update() = {
    //get all torches
    val torches = CastleUtil.getAllRoomsContainingItem(castle, classOf[Torch])
    //set baseline darkness on all tiles in rooms with torches
    torches.foreach(torchTile => setBaseline(torchTile.item.get.asInstanceOf[Torch]))

    torches.foreach(torchTile => handleRoom(torchTile.item.get.asInstanceOf[Torch]))
  }

  private def setBaseline(torch: Torch) {
    val affectedTiles = filterOutWalls(CastleUtil.getFloorsSharingRoomIds(castle, torch.container.roomIDs, true))

    affectedTiles.foreach { tile =>
      tile.darkness = .85f
    }
  }

  private def handleRoom(torch: Torch) {
    val availableTiles = filterOutWalls(CastleUtil.getFloorsSharingRoomIds(castle, torch.container.roomIDs, true))

    (1 to torch.luminosity).foreach { radii =>
      availableTiles.filter(tile => CastleUtil.distanceBetweenTiles(torch.container.getTilePosition, tile.getTilePosition) <= radii).foreach { tile =>
        torch.lit match {
          case true => tile.darkness = scala.math.max(tile.darkness - torch.brightnessDecrement, 0) //if it's going to be negative, make it 0 (max brightness)
          case false => //tile.darkness = .8f
        }
      }
    }

  }

  private def filterOutWalls(floors: List[Floor]): List[Floor] = {
    floors.filter(tile => tile.item match {
      case Some(itm) => itm match {
        case w: Wall => false
        case _ => true
      }
      case None => true
    })
  }
}