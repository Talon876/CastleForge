package org.nolat.castleforge.castle.items.attributes

import org.nolat.castleforge.castle.PlayerListener
import org.nolat.castleforge.castle.items.Item
import org.nolat.castleforge.castle.Player
import org.nolat.castleforge.castle.CastleUtil
import org.nolat.castleforge.castle.Floor
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.state.StateBasedGame
import org.nolat.castleforge.ui.HUD

trait Readable extends Item with PlayerListener {
  var displayText = ""
}