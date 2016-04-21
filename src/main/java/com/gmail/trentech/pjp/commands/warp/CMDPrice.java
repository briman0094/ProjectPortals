package com.gmail.trentech.pjp.commands.warp;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjp.portals.Warp;
import com.gmail.trentech.pjp.utils.ConfigManager;
import com.gmail.trentech.pjp.utils.Help;

public class CMDPrice implements CommandExecutor {

	public CMDPrice(){
		String alias = new ConfigManager().getConfig().getNode("settings", "commands", "warp").getString();
		
		Help help = new Help("wprice", "price", " Charge players for using warps. 0 to disable");
		help.setSyntax(" /warp price <name> <price>\n /" + alias + " p <name> <price>");
		help.setExample(" /warp price Lobby 50\n /warp price Lobby 0");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!(src instanceof Player)){
			src.sendMessage(Text.of(TextColors.DARK_RED, "Must be a player"));
			return CommandResult.empty();
		}

		if(!args.hasAny("name")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		String name = args.<String>getOne("name").get();

		if(!Warp.get(name).isPresent()){
			src.sendMessage(Text.of(TextColors.DARK_RED, name, " does not exist"));
			return CommandResult.empty();
		}	
		Warp warp = Warp.get(name).get();
		
		if(!args.hasAny("price")) {
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}
		
		double price;
		try{
			price = Double.parseDouble(args.<String>getOne("price").get());
		}catch(Exception e){
			src.sendMessage(invalidArg());
			return CommandResult.empty();
		}

		warp.setPrice(price);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set price of warp ", warp.getName(), " to $", price));
		
		return CommandResult.success();
	}
	
	private Text invalidArg(){
		Text t1 = Text.of(TextColors.YELLOW, "/warp price <name> ");
		Text t2 = Text.builder().color(TextColors.YELLOW).onHover(TextActions.showText(Text.of("Enter a number amount or 0 to disable"))).append(Text.of("<price>")).build();
		return Text.of(t1,t2);
	}
}