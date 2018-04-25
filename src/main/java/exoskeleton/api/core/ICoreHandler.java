package exoskeleton.api.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ICoreHandler{
    void onUpdate(EntityPlayer player, ItemStack stack);
}