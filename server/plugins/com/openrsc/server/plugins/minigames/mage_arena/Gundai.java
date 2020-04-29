package com.openrsc.server.plugins.minigames.mage_arena;

import com.openrsc.server.constants.IronmanMode;
import com.openrsc.server.constants.NpcId;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.plugins.triggers.OpNpcTrigger;
import com.openrsc.server.plugins.triggers.TalkNpcTrigger;
import com.openrsc.server.plugins.menu.Menu;
import com.openrsc.server.plugins.menu.Option;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.openrsc.server.plugins.Functions.*;

public class Gundai implements TalkNpcTrigger, OpNpcTrigger {
	private static final Logger LOGGER = LogManager.getLogger(Gundai.class);
	@Override
	public void onTalkNpc(final Player player, final Npc n) {
		say(player, n, "hello, what are you doing out here?");
		npcsay(player, n, "why i'm a banker, the only one around these dangerous parts");
		Menu defaultMenu = new Menu();
		defaultMenu.addOption(new Option("cool, I'd like to access my bank account please") {
			@Override
			public void action() {
				if (player.isIronMan(IronmanMode.Ultimate.id())) {
					player.message("As an Ultimate Iron Man, you cannot use the bank.");
					return;
				}

				if(validatebankpin(player)) {
					npcsay(player, n, "no problem");
					player.setAccessingBank(true);
					ActionSender.showBank(player);
				}
			}
		});
		if (player.getWorld().getServer().getConfig().WANT_BANK_PINS) {
			defaultMenu.addOption(new Option("I'd like to talk about bank pin") {
				@Override
				public void action() {
					int menu = multi(player, "Set a bank pin", "Change bank pin", "Delete bank pin");
					if (menu == 0) {
						setbankpin(player);
					} else if (menu == 1) {
						changebankpin(player);
					} else if (menu == 2) {
						removebankpin(player);
					}
				}
			});
		}

		if (player.getWorld().getServer().getConfig().SPAWN_AUCTION_NPCS) {
			defaultMenu.addOption(new Option("I'd like to collect my items from auction") {
				@Override
				public void action() {
					if(validatebankpin(player)) {
						player.getWorld().getMarket().addPlayerCollectItemsTask(player);
					}
				}
			});
		}

		defaultMenu.addOption(new Option("Well, now i know") {
			@Override
			public void action() {
				npcsay(player, n, "knowledge is power my friend");
			}
		});
		defaultMenu.showMenu(player);
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.GUNDAI.id();
	}

	@Override
	public void onOpNpc(Player player, Npc n, String command) {
		if (n.getID() == NpcId.GUNDAI.id()) {
			if (command.equalsIgnoreCase("Bank")) {
				quickFeature(n, player, false);
			} else if (player.getWorld().getServer().getConfig().SPAWN_AUCTION_NPCS && command.equalsIgnoreCase("Collect")) {
				quickFeature(n, player, true);
			}
		}
	}

	@Override
	public boolean blockOpNpc(Player player, Npc n, String command) {
		if (n.getID() == NpcId.GUNDAI.id() && command.equalsIgnoreCase("Bank")) {
			return true;
		}
		if (n.getID() == NpcId.GUNDAI.id() && player.getWorld().getServer().getConfig().SPAWN_AUCTION_NPCS && command.equalsIgnoreCase("Collect")) {
			return true;
		}
		return false;
	}

	private void quickFeature(Npc npc, Player player, boolean auction) {
		if (player.isIronMan(IronmanMode.Ultimate.id())) {
			player.message("As an Ultimate Iron Man, you cannot use the bank.");
			return;
		}

		if(validatebankpin(player)) {
			if (player.getWorld().getServer().getConfig().SPAWN_AUCTION_NPCS && auction) {
				player.getWorld().getMarket().addPlayerCollectItemsTask(player);
			} else {
				player.setAccessingBank(true);
				ActionSender.showBank(player);
			}
		}
	}

}
