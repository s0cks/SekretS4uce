package exoskeleton.common;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;

public interface IExoskeletonProxy{
    default Minecraft getClient(){
        return null;
    }

    default void registerItemRenderer(Item item, int idx, String name){}
}