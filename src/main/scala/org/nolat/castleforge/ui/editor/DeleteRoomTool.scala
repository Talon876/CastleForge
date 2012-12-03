package org.nolat.castleforge.ui.editor

import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.GameContainer
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.Graphics
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.items.Item

class DeleteRoomTool(x: Int, y: Int, castle: Castle, container: GameContainer) extends Tool(x, y, castle, container) {

  override def apply(region: List[List[Floor]]) {

    val listOfRoomIdLists = region.flatten.map { floor =>
      floor.roomIDlist
    }

    val commonRoomIdList = listOfRoomIdLists.foldLeft(listOfRoomIdLists(0)) { _ intersect _ } //gets common room id
    if (commonRoomIdList.size == 1) { //there has to just be one common room id
      val commonRoomId = commonRoomIdList(0)
      if (commonRoomId != 0) { //the room id can't be 0
        println("Deleting room " + commonRoomId)
        val perimeter = CastleUtil.getRoomPerimeter(region)
        val inside = CastleUtil.getRoomInsideRegion(region)

        //the inside tiles must have a id list of length 1
        val validInside = inside.filter { floor =>
          floor.roomIDlist.size != 1
        }.size == 0

        var validPerimeter = true
        perimeter.foreach { floor =>

          // println(validPerimeter)
          if (floor.roomIDlist.size == 1) {
            validPerimeter = validPerimeter && floor.roomIDlist(0) == commonRoomId && floor.itemName == "wall"
          } else {
            validPerimeter = validPerimeter && floor.roomIDlist.size >= 2
          }

        }

        //perimeter and inside must be valid in order to delete
        if (validInside && validPerimeter) {
          //room deletion

          inside.foreach { floor =>
            floor.roomIDlist = List(0) //set back to empty
            CastleUtil.removeItem(castle, floor.getTilePosition) //delete the item
          }

          perimeter.foreach { floor =>
            if (floor.roomIDlist.size == 1) {
              floor.roomIDlist = List(0)
              CastleUtil.removeItem(castle, floor.getTilePosition)
            }

            if (floor.roomIDlist.size == 3) {
              //weird case
            }

            if (floor.roomIDlist.size == 2 && floor.roomIDlist.contains(0)) {
              CastleUtil.removeItem(castle, floor.getTilePosition)
              floor.roomIDlist = List(0)
            }

            //if length is 2, replace roomiddeleted with 0
            if (floor.roomIDlist.size == 2) {
              val listWithoutDeletedId = floor.roomIDlist.filter {
                i => i != commonRoomId
              }
              floor.roomIDlist = List(listWithoutDeletedId, List(0)).flatten
              CastleUtil.addItem(castle, floor.getTilePosition, Item("wall"))
            }

            //if length is >2, remove roomiddeleted
            if (floor.roomIDlist.size > 2) {
              val listWithoutDeletedId = floor.roomIDlist.filter {
                i => i != commonRoomId
              }
              floor.roomIDlist = listWithoutDeletedId
            }
          }

        } else {
          //println("invalid (perimeter, inside): " + validPerimeter + " " + validInside)
        }

      } else {
        //println("invalid selection")
      }
    } else {
      //println("invalid selection")
    }

  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    Tool.deleteroom.draw(x, y)
    super.render(container, game, g)
  }
}