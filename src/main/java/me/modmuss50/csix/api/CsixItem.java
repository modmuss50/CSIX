package me.modmuss50.csix.api;

import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class CsixItem {
	public String registryName;
	public int stackSize;
	public int meta;
	public String nbt;
	public String modid;
	public String UUID;


	public ItemStack toStack() throws NumberInvalidException, NBTException {
		Item item = CommandBase.getItemByText(null, registryName);
		ItemStack stack = new ItemStack(item, stackSize, meta);
		if(!nbt.isEmpty()){
			stack.setTagCompound(JsonToNBT.getTagFromJson(nbt));
		}
		return stack;
	}

	public static CsixItem fromStack(ItemStack stack){
		CsixItem item = new CsixItem();

		item.registryName = stack.getItem().getRegistryName().toString();
		item.stackSize = stack.getCount();
		item.meta = stack.getMetadata();
		if(stack.hasTagCompound()){
			item.nbt = stack.getTagCompound().toString();
		}
		item.modid = stack.getItem().getRegistryName().getResourceDomain();

		return item;
	}

}
