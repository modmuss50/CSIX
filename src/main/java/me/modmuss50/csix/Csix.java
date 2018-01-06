package me.modmuss50.csix;

import me.modmuss50.csix.api.CsixItem;
import me.modmuss50.csix.api.RemoveResponse;
import me.modmuss50.csix.api.WebHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.io.IOException;

@Mod(modid = "csix", name = "Cross Server Item eXchange")
public class Csix {

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new Upload());
		event.registerServerCommand(new Coins());
		event.registerServerCommand(new Remove());
	}

	public static class Upload extends CommandBase {

		@Override
		public String getName() {
			return "upload";
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return "upload";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			EntityPlayer player = (EntityPlayer) sender;
			try {
				if(!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()){
					WebHandler.addItem(player.getHeldItem(EnumHand.MAIN_HAND), player);
				}

				for(CsixItem item : WebHandler.listItems()){
					sender.sendMessage(new TextComponentString(item.registryName + ":" + item.UUID));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Coins extends CommandBase {

		@Override
		public String getName() {
			return "coins";
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return "coins";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			EntityPlayer player = (EntityPlayer) sender;
			try {
				sender.sendMessage(new TextComponentString("You have " + WebHandler.getCoins(player) + " coins"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Remove extends CommandBase {

		@Override
		public String getName() {
			return "remove";
		}

		@Override
		public String getUsage(ICommandSender sender) {
			return "remove";
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			EntityPlayer player = (EntityPlayer) sender;
			try {
				RemoveResponse removeResponse = WebHandler.removeItem(args[0], player);
				player.inventory.addItemStackToInventory(removeResponse.item.toStack());
				sender.sendMessage(new TextComponentString("You have " + WebHandler.getCoins(player) + " coins"));
			} catch (IOException | NBTException e) {
				e.printStackTrace();
			}
		}
	}

}
