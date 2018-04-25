package exoskeleton.common.core;

import exoskeleton.api.IExoskeletonApi;
import exoskeleton.api.core.ICoreHandler;
import exoskeleton.common.util.ExoskeletonTag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Optional;

public final class CoreEventHandler{
    private CoreEventHandler(){}

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent e){
        if(e.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntityLiving();
            ((List<ItemStack>) player.getArmorInventoryList())
                    .stream()
                    .filter(ExoskeletonTag::hasStackTag)
                    .forEach((s) ->{
                        Optional<ICoreHandler> ch = IExoskeletonApi.getInstance()
                                .getCoreHandlerFor(s);
                        ch.ifPresent((handler)->handler.onUpdate(player, s));
                    });
        }
    }
}