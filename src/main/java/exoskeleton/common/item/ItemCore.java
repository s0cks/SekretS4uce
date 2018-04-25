package exoskeleton.common.item;

import exoskeleton.common.Exoskeleton;
import net.minecraft.item.Item;

public final class ItemCore
extends Item{
    public ItemCore(){
        this.setMaxStackSize(1);
        this.setCreativeTab(Exoskeleton.tab);
        this.setUnlocalizedName("core");
        this.setRegistryName("core");
    }

    public void registerItemModel(){
        Exoskeleton.proxy.registerItemRenderer(
                this,
                0,
                "core"
        );
    }
}