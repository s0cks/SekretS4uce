package exoskeleton.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public final class ExoskeletonItems{
    public static final ItemCore ITEM_CORE = new ItemCore();

    public static void registerAllItems(IForgeRegistry<Item> registry){
        registry.registerAll(
                ITEM_CORE
        );
    }

    public static void registerAllModels(){
        ITEM_CORE.registerItemModel();
    }
}