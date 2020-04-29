package com.openrsc.server.plugins.misc;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.model.entity.GameObject;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.triggers.OpLocTrigger;

import static com.openrsc.server.plugins.Functions.*;

public class LeafyPalmTree implements OpLocTrigger {

	@Override
	public boolean blockOpLoc(Player player, GameObject obj, String command) {
		return obj.getID() == 1176;
	}

	@Override
	public void onOpLoc(Player player, GameObject obj, String command) {
		if (obj.getID() == 1176) {
			mes(player, player.getWorld().getServer().getConfig().GAME_TICK * 2, "You give the palm tree a good shake.");
			mes(player, 0, "A palm leaf falls down.");
			addobject(ItemId.PALM_TREE_LEAF.id(), 1, obj.getX(), obj.getY(), player);
			changeloc(obj, 15000, 33);
		}
	}
}
