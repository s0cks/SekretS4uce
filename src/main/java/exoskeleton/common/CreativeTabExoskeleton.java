package exoskeleton.common;

import exoskeleton.common.item.ExoskeletonItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

final class CreativeTabExoskeleton
extends CreativeTabs{
    CreativeTabExoskeleton(){
        super("exoskeleton");
    }

    @Override
    public ItemStack getTabIconItem(){
        return new ItemStack(ExoskeletonItems.ITEM_CORE, 1);
    }
}