package com.gmail.trentech.pjp.commands.portal;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.gmail.trentech.pjp.Main;
import com.gmail.trentech.pjp.listeners.LegacyListener;
import com.gmail.trentech.pjp.portal.LegacyBuilder;
import com.gmail.trentech.pjp.utils.Help;

public class CMDSave implements CommandExecutor {

	public CMDSave() {
		Help help = new Help("save", "save", " Saves generated portal");
		help.setPermission("pjp.cmd.portal.save");
		help.setSyntax(" /portal save\n /p s");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;

		if (!LegacyListener.builders.containsKey(player.getUniqueId())) {
			throw new CommandException(Text.of(TextColors.DARK_GREEN, "Not in build mode"), false);
		}
		LegacyBuilder builder = LegacyListener.builders.get(player.getUniqueId());

		if (!builder.isFill()) {
			builder.setFill(true);
			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Portal frame saved"));
			player.sendMessage(Text.builder().color(TextColors.DARK_GREEN).append(Text.of("Begin filling in portal frame, followed by ")).onClick(TextActions.runCommand("/pjp:portal save")).append(Text.of(TextColors.YELLOW, TextStyles.UNDERLINE, "/portal save")).build());
			return CommandResult.success();
		}

		if (builder.build()) {
			Sponge.getScheduler().createTaskBuilder().name("PJP" + builder.getName()).delayTicks(20).execute(t -> {
				LegacyListener.builders.remove(player.getUniqueId());
			}).submit(Main.getPlugin());

			player.sendMessage(Text.of(TextColors.DARK_GREEN, "Portal ", builder.getName(), " created successfully"));
		}

		return CommandResult.success();
	}
}
