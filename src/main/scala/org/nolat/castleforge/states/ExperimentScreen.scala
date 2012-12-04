package org.nolat.castleforge.states

import java.io.File
import scala.collection.mutable.ArrayBuffer
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.geom.Vector2f
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.newdawn.slick.state.transition.FadeInTransition
import org.newdawn.slick.state.transition.FadeOutTransition
import org.nolat.castleforge.castle.Castle
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.Floor
import org.nolat.castleforge.castle.items.CheckPoint
import org.nolat.castleforge.castle.items.CrystalBall
import org.nolat.castleforge.castle.items.Door
import org.nolat.castleforge.castle.items.EndPoint
import org.nolat.castleforge.castle.items.Ice
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.items.Key
import org.nolat.castleforge.castle.items.Match
import org.nolat.castleforge.castle.items.Obstacle
import org.nolat.castleforge.castle.items.Pusher
import org.nolat.castleforge.castle.items.Sign
import org.nolat.castleforge.castle.items.SpawnPoint
import org.nolat.castleforge.castle.items.Teleporter
import org.nolat.castleforge.castle.items.Torch
import org.nolat.castleforge.castle.items.Wall
import org.nolat.castleforge.tools.Lerper
import org.nolat.castleforge.ui.ElementInventory
import org.nolat.castleforge.ui.ElementPlayerDebug
import org.nolat.castleforge.ui.ElementSign
import org.nolat.castleforge.ui.HUD
import org.nolat.castleforge.ui.HUDElement
import org.nolat.castleforge.xml.MapSave
import org.nolat.castleforge.castle.ExpansionDirection
import org.nolat.castleforge.ui.MainHUD

object ExperimentScreen {
  val ID = 3
  val castleWidth = 25
  val items = List("checkpoint", "door",
    "endpoint",
    "key",
    "match",
    "obstacle",
    "pusher",
    "sign",
    "spawnpoint",
    "teleporter",
    "torch",
    "crystal_ball",
    "ice",
    "wall")
  val colors = List(
    "white",
    "red",
    "orange",
    "yellow",
    "green",
    "blue",
    "purple")
  val quantities = List[Int](0,
    1,
    3,
    5,
    10,
    15,
    20)
  val shapes = List("diamond",
    "pentagon",
    "triangle",
    "star")

  val lumenosity = List("low",
    "medium",
    "high")

  val direction = List("north",
    "west",
    "south",
    "east")
  val checkpointState = List(true, false)
  val torchState = List(true, false)
  val doorType = List[Int](0, 1, 2)
  val obstacles = List("hole",
    "type2",
    "type3",
    "type4",
    "type5",
    "type6",
    "type7",
    "type8")
  val teleType = List[String]("sender", "receiver", "bidirectional")

  def list2Floors(items: List[Item]): ArrayBuffer[ArrayBuffer[Floor]] = {
    val floors: ArrayBuffer[ArrayBuffer[Floor]] = new ArrayBuffer[ArrayBuffer[Floor]]
    var row: Int = 0
    var temp: ArrayBuffer[Floor] = new ArrayBuffer[Floor]
    var col: Int = 0;
    items.grouped(ExperimentScreen.castleWidth).foreach { i =>
      i.foreach { itm =>
        temp.append(new Floor(Some(itm), col, row, "1"))
        col += 1
      }
      floors.append(temp)
      temp = new ArrayBuffer[Floor]
      col = 0;
      row += 1
    }
    var lastRow = floors(floors.length - 1)
    while (lastRow.length < ExperimentScreen.castleWidth) {
      lastRow.append(new Floor(None, lastRow.length, floors.length - 1, "0"))
    }
    floors
  }
  def allCombinations(): List[Item] = {
    var list: ArrayBuffer[Item] = new ArrayBuffer[Item]
    createAllCheckpoints(list)
    createAllDoors(list)
    createAllEndpoints(list)
    createAllKeys(list)
    createAllMatches(list)
    createAllObstacles(list)
    createSign(list)
    createAllSpawnPoint(list)
    createAllTeleporters(list)
    createAllTorches(list)
    createCrystalBall(list)
    createIce(list)
    createWall(list)

    list.toList
  }
  def createAllCheckpoints(list: ArrayBuffer[Item]) {
    //ExperimentScreen.checkpointState.foreach {
    // b => list.append(new CheckPoint(b))
    //}
    list.append(new CheckPoint(false))
    list.append(new CheckPoint(false))
  }

  def createAllDoors(list: ArrayBuffer[Item]) {
    ExperimentScreen.doorType.foreach { dt =>
      dt match {
        case 1 => ExperimentScreen.colors.foreach { clr =>
          ExperimentScreen.shapes.foreach {
            shp => list.append(new Door(dt, clr, shp))
          }
        }
        case 0 => list.append(new Door(dt))
        case 2 => list.append(new Door(dt))
      }

    }
  }

  def createAllEndpoints(list: ArrayBuffer[Item]) {
    list.append(new EndPoint("You Win!!!"))
  }

  def createAllKeys(list: ArrayBuffer[Item]) {
    //ExperimentScreen.quantities.foreach { quan =>
    ExperimentScreen.colors.foreach { clr =>
      ExperimentScreen.shapes.foreach {
        shp => list.append(new Key(clr, shp, 1))
      }
    }
    //}
  }

  def createAllMatches(list: ArrayBuffer[Item]) {
    ExperimentScreen.quantities.foreach { quan =>
      list.append(new Match(quan))
    }
  }

  def createAllObstacles(list: ArrayBuffer[Item]) {
    ExperimentScreen.obstacles.foreach { ob =>
      list.append(new Obstacle(ob))
    }
  }
  def createAllPusher(list: ArrayBuffer[Item]) {
    ExperimentScreen.direction.foreach {
      dir => list.append(new Pusher(dir))
    }
  }

  def createSign(list: ArrayBuffer[Item]) {
    list.append(new Sign("Yo Bro"))
  }

  def createAllSpawnPoint(list: ArrayBuffer[Item]) {
    // ExperimentScreen.checkpointState.foreach {
    // b => list.append(new SpawnPoint(b))
    // }
    list.append(new SpawnPoint(true))
  }
  def createAllTeleporters(list: ArrayBuffer[Item]) {
    //ExperimentScreen.teleType.foreach { typ =>
    ExperimentScreen.colors.foreach { clr =>
      list.append(new Teleporter("bidirectional", clr))
    }
    ExperimentScreen.colors.foreach { clr =>
      list.append(new Teleporter("bidirectional", clr))
    }
    // }
  }

  def createAllTorches(list: ArrayBuffer[Item]) {
    ExperimentScreen.lumenosity.foreach { lum =>
      ExperimentScreen.colors.foreach { clr =>
        //ExperimentScreen.torchState.foreach { st =>
        list.append(new Torch(true, lum, clr))
        //}
      }
    }
  }

  def createCrystalBall(list: ArrayBuffer[Item]) {
    list.append(new CrystalBall())
  }

  def createIce(list: ArrayBuffer[Item]) {
    list.append(new Ice())
  }
  def createWall(list: ArrayBuffer[Item]) {
    list.append(new Wall())
  }
}
class ExperimentScreen extends BasicGameState {
  var game: StateBasedGame = null

  override def getID = ExperimentScreen.ID

  var castle: Castle = null

  var hud: MainHUD = null

  var lstItem = ArrayBuffer[Item]()

  val authorName = "Steve"
  val mapName = "Test Map"
  var loaded = false;
  override def enter(container: GameContainer, game: StateBasedGame) {
    loaded = false;
    if (SharedStateData.mapFile == null) { //hasn't loaded from the File
      castle = Castle(ExperimentScreen.list2Floors(ExperimentScreen.allCombinations()), mapName, authorName, "This is a test Map")
    } else {
      SharedStateData.loadedCastle = SharedStateData.mapFile //load the map
      castle = SharedStateData.loadedCastle //grab loaded castle
      SharedStateData.mapFile = null
    }
    castle.isEditor = true
    hud = new MainHUD(castle, game)
    loaded = true;
  }
  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (!container.isPaused) {
      Lerper.lerpers.foreach(_.update(delta))
      castle.update(container, game, delta)
    }

    if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.setPaused(!container.isPaused)
    hud.update(container, game, delta)
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    if (loaded) {
      g.setBackground(Color.black)
      g.setColor(Color.white)
      castle.render(container, game, g)
      g.setColor(Color.black)
      hud.render(container, game, g)
    }
  }

  override def keyReleased(key: Int, c: Char) {
    if (key == Input.KEY_F9) {
      MapSave.save(Castle(ExperimentScreen.list2Floors(ExperimentScreen.allCombinations()), mapName, authorName, "This is a test Map"), true)
      //SharedStateData.mapFile = saveLocation
      SharedStateData.loadOriginal = false
      game.enterState(ExperimentScreen.ID, new FadeOutTransition(), new FadeInTransition())
    } else if (key == Input.KEY_F10) {
      SharedStateData.mapFile = castle.fileLocation
      SharedStateData.loadOriginal = false
      game.enterState(ExperimentScreen.ID, new FadeOutTransition(), new FadeInTransition())
    } else if (key == Input.KEY_F8) {
      CastleUtil.expandCastle(castle, ExpansionDirection.ALL, 5)
    } else if (key == Input.KEY_F7) {
      CastleUtil.expandCastle(castle, ExpansionDirection.LEFT, 5)
      CastleUtil.expandCastle(castle, ExpansionDirection.RIGHT, 5)
      CastleUtil.expandCastle(castle, ExpansionDirection.TOP, 5)
      CastleUtil.expandCastle(castle, ExpansionDirection.BOTTOM, 5)
    }
  }
}