package exoskeleton.common.core.handlers;

import exoskeleton.api.IExoskeletonApi;
import exoskeleton.api.core.ICoreHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class CoreHandlerRecon
implements ICoreHandler{
    @Override
    public void onUpdate(EntityPlayer player, ItemStack stack){
        IExoskeletonApi.getInstance()
                .getPlayerArmorData(player, stack)
                .ifPresent((data)->{

                });
    }
}