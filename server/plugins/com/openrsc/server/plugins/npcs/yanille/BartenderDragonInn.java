package com.openrsc.server.plugins.npcs.yanille;

import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.triggers.TalkNpcTrigger;
import com.openrsc.server.plugins.menu.Menu;
import com.openrsc.server.plugins.menu.Option;

import static com.openrsc.server.plugins.Functions.*;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.NpcId;

public final class BartenderDragonInn implements
	TalkNpcTrigger {

	@Override
	public void onTalkNpc(final Player player, final Npc n) {
		if (n.getID() == NpcId.BARTENDER_YANILLE.id()) {
			npcsay(player, n, "What can I get you?");
			say(player, n, "What's on the menu?");
			npcsay(player, n, "Dragon bitter and Greenmans ale");
			Menu defaultMenu = new Menu();
			defaultMenu.addOption(new Option("I'll give it a miss I think") {
				@Override
				public void action() {
					npcsay(player, n, "Come back when you're a little thirstier");
				}
			});
			defaultMenu.addOption(new Option("I'll try the dragon bitter") {
				@Override
				public void action() {
					npcsay(player, n, "Ok, that'll be two coins");
					if (ifheld(player, ItemId.COINS.id(), 2)) {
						player.message("You buy a pint of dragon bitter");
						give(player, ItemId.DRAGON_BITTER.id(), 1);
						player.getCarriedItems().remove(new Item(ItemId.COINS.id(), 2));
					} else {
						say(player, n, "Oh dear. I don't seem to have enough money");
					}
				}
			});
			defaultMenu.addOption(new Option("Can I have some greenmans ale?") {
				@Override
				public void action() {
					npcsay(player, n, "Ok, that'll be ten coins");
					if (ifheld(player, ItemId.COINS.id(), 10)) {
						player.message("You buy a pint of ale");
						give(player, ItemId.GREENMANS_ALE.id(), 1);
						player.getCarriedItems().remove(new Item(ItemId.COINS.id(), 10));
					} else {
						say(player, n, "Oh dear. I don't seem to have enough money");
					}
				}
			});
			defaultMenu.showMenu(player);
		}
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.BARTENDER_YANILLE.id();
	}

}
