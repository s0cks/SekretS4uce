package exoskeleton.common;

import exoskeleton.api.IExoskeletonApi;
import exoskeleton.api.skill.SkillTree;
import exoskeleton.common.item.ExoskeletonItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(
        modid = "exo",
        name = "Exoskeleton",
        version = "1.0.0",
        useMetadata = true
)
public final class Exoskeleton
implements IExoskeletonApi{
    @SidedProxy(
            clientSide = "exoskeleton.client.ClientProxy",
            serverSide = "exoskeleton.server.ServerProxy"
    )
    public static IExoskeletonProxy proxy;

    public static final CreativeTabs tab = new CreativeTabExoskeleton();

    @Override
    public SkillTree getSkillTree(String tag) {
        return null;
    }

    @Override
    public void registerSkillTree(String tag, SkillTree tree) {

    }

    @Mod.EventBusSubscriber
    public static final class RegistrationHandler{
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> e){
            ExoskeletonItems.registerAllItems(e.getRegistry());
        }

        @SubscribeEvent
        public static void registerItemModels(ModelRegistryEvent e){
            ExoskeletonItems.registerAllModels();
        }
    }
}