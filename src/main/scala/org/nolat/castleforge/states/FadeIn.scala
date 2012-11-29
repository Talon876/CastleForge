package org.nolat.castleforge.states

import org.newdawn.slick.Graphics
import org.nolat.castleforge.Config
import org.newdawn.slick.Color

trait FadeIn {
  var fadingIn = false
  var opacity = 1f

  def handleFade(g: Graphics) {
    if (fadingIn) {
      g.setColor(new Color(0, 0, 0, opacity))
      g.fillRect(0, 0, Config.Resolution.getX, Config.Resolution.getY)
      opacity -= .01f
      if (opacity < 0f) {
        fadingIn = false
        opacity = 1f
      }
    }
  }
}