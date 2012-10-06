package org.nolat.castleforge.states

import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.Config
import org.newdawn.slick.geom.Vector2f
import org.nolat.castleforge.ui.Menu
import org.nolat.castleforge.graphics.Loader
import org.newdawn.slick.Input
import org.nolat.castleforge.graphics.Sprite
import org.nolat.castleforge.graphics.Sprites
import scala.collection.mutable.MutableList

object ExperimentScreen {
  val ID = 3
}
class ExperimentScreen extends BasicGameState {
  var game: StateBasedGame = null
  var sprites: MutableList[Sprite] = new MutableList[Sprite]()

  override def getID = ExperimentScreen.ID

  override def init(container: GameContainer, game: StateBasedGame) {
    this.game = game

    Sprites.values.foreach { spriteName =>
      sprites.+=(new Sprite(spriteName))
    }
  }

  override def update(container: GameContainer, game: StateBasedGame, delta: Int) {
    if (container.getInput().isKeyPressed(Input.KEY_1)) {

    } else if (container.getInput().isKeyPressed(Input.KEY_2)) {

    } else if (container.getInput().isKeyPressed(Input.KEY_3)) {

    }
  }

  override def render(container: GameContainer, game: StateBasedGame, g: Graphics) {
    g.setBackground(Color.black)
    g.setColor(Color.white)
    for (y <- 0 to 10) {
      for (x <- 0 to 18) {
        sprites(4).setAnimation("type1")
        sprites(4).getAnimation.draw(x * Config.TileWidth, y * Config.TileHeight)
      }
    }
    var c = 0
    for (y <- 0 to 10 by 2) {
      for (x <- 0 to 18) {
        //floor.getAnimation.draw(x * Config.TileWidth, y * Config.TileHeight)

        if (c < sprites.size) {
          sprites(c).animationList.foreach { animation =>
            sprites(c).setAnimation(animation)
            sprites(c).getAnimation.draw(x * Config.TileWidth, (y + sprites(c).animationList.indexOf(animation)) * Config.TileHeight)
          }
        }
        c += 1
      }
    }
  }

  override def keyReleased(key: Int, c: Char) {

  }
}