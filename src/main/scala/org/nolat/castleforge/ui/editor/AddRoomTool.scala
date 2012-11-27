package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Castle
import org.newdawn.slick.GameContainer
import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Item

class AddRoomTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  var id = 2

  override def apply(region: List[List[Floor]]) {
    val width = region.size
    val height = region(0).size
    if (width >= 3 && height >= 3) {

      val perim = CastleUtil.getRoomPerimeter(region)
      val inside = CastleUtil.getRoomInsideRegion(region)

      val validPerim = perim.filter { floor =>
        floor.roomIDlist.size == 2 || //has to be a border
          floor.roomIDlist.size == 3 || //has to be a border
          floor.roomIDs == "0" //has to not be used already
      }.size == perim.size

      val validInside = inside.filter { floor =>
        floor.roomIDs != "0"
      }.size == 0

      if (validPerim && validInside) {

        perim.foreach { border =>
          CastleUtil.addItem(castle, border.getTilePosition, Item("wall"))

          border.roomIDlist = List(border.roomIDlist, List(id)).flatten //ad the current id

          val listWithout0 = border.roomIDlist.filter(i => i != 0)
          border.roomIDlist = border.roomIDlist.filter(i => i != 0) //remove the 0
        }

        inside.foreach { floor =>
          floor.roomIDlist = List(id)
        }

        //add 0 to ones where necessary (an adjancent tile id is == "0")
        perim.foreach { border =>
          //println("neighborlist of tiles at " + border.getTilePosition + ": " + CastleUtil.getNeighbors(castle, border).map(_.getTilePosition) + " ")
          CastleUtil.getNeighbors(castle, border).foreach { neighbor =>
            //println("neighbor ids for " + neighbor.getTilePosition + ": " + neighbor.roomIDs + " (" + (neighbor.roomIDs == "0") + ")")
            if (neighbor.roomIDs == "0") {

              border.roomIDlist = List(border.roomIDlist, List(0)).flatten
              border.roomIDlist = border.roomIDlist.distinct
            }

          }
        }

        id += 1
      } else {
        println("inside perim: " + validInside + " " + validPerim)
      }

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Tool.addroom.draw(x, y)
    super.render(container, game, g)
  }
}