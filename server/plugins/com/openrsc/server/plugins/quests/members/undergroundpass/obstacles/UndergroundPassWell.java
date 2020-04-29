package com.openrsc.server.plugins.quests.members.undergroundpass.obstacles;

import com.openrsc.server.constants.Quests;
import com.openrsc.server.constants.Skills;
import com.openrsc.server.model.entity.GameObject;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.triggers.OpLocTrigger;

import static com.openrsc.server.plugins.Functions.*;

public class UndergroundPassWell implements OpLocTrigger {

	public static final int WELL = 814;

	@Override
	public boolean blockOpLoc(Player player, GameObject obj, String command) {
		return obj.getID() == WELL;
	}

	@Override
	public void onOpLoc(Player player, GameObject obj, String command) {
		if (obj.getID() == WELL) {
			mes(player, "you climb into the well");
			if ((player.getCache().hasKey("orb_of_light1") && player.getCache().hasKey("orb_of_light2") && player.getCache().hasKey("orb_of_light3") && player.getCache().hasKey("orb_of_light4")) ||
					atQuestStages(player, Quests.UNDERGROUND_PASS, 7, 8, -1)) {
				mes(player, "you feel the grip of icy hands all around you...");
				player.teleport(722, 3461);
				delay(player.getWorld().getServer().getConfig().GAME_TICK);
				displayTeleportBubble(player, player.getX(), player.getY(), true);
				player.message("..slowly dragging you futher down into the caverns");
			} else {
				player.damage((int) (getCurrentLevel(player, Skills.HITS) * 0.2D));
				displayTeleportBubble(player, obj.getX(), obj.getY(), false);
				mes(player, "from below an icy blast of air chills you to your bones",
					"a mystical force seems to blast you back out of the well");
				player.message("there must be a positive force near by!");
			}
		}
	}
}
