package exoskeleton.client;

import exoskeleton.common.IExoskeletonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;

final class ClientProxy
implements IExoskeletonProxy{
    @Override
    public Minecraft getClient() {
        return FMLClientHandler.instance()
                .getClient();
    }

    @Override
    public void registerItemRenderer(Item item, int idx, String name){
        ModelLoader.setCustomModelResourceLocation(
                item,
                idx,
                new ModelResourceLocation("exoskeleton:" + name, "inventory")
        );
    }
}