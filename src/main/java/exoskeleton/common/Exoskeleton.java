package exoskeleton.common;

import exoskeleton.api.IExoskeletonApi;
import exoskeleton.api.armor.PlayerArmorData;
import exoskeleton.api.armor.PlayerArmorSlot;
import exoskeleton.api.core.ICoreHandler;
import exoskeleton.api.skill.SkillTree;
import exoskeleton.common.core.CoreRegistry;
import exoskeleton.common.item.ExoskeletonItems;
import exoskeleton.common.util.ExoskeletonTag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<SkillTree> getSkillTreeFor(String tag){
        return CoreRegistry.getSkillTreeFor(tag);
    }

    @Override
    public Optional<ICoreHandler> getCoreHandlerFor(String tag){
        return CoreRegistry.getCoreHandlerFor(tag);
    }

    @Override
    public Optional<ICoreHandler> getCoreHandlerFor(ItemStack stack){
        return ExoskeletonTag.getStackTag(stack)
                .map((c)->{
                    c = c.getCompoundTag("Core");
                    return CoreRegistry.getCoreHandlerFor(c.getString("Name"))
                            .orElseGet(()->null);
                });
    }

    @Override
    public Optional<PlayerArmorData> getPlayerArmorData(EntityPlayer player, ItemStack stack){
        return ExoskeletonTag.getStackTag(stack)
                .map((tag)->new PlayerArmorData(player, tag));
    }

    @Override
    public Optional<PlayerArmorData> getPlayerArmorData(EntityPlayer player, PlayerArmorSlot slot) {
        return ((List<ItemStack>) player.getArmorInventoryList())
                .stream()
                .filter(PlayerArmorSlot.isSlot(slot))
                .map(ExoskeletonTag::getStackTag)
                .filter(Optional::isPresent)
                .map((tag)->new PlayerArmorData(player, tag.get()))
                .findAny();
    }

    @Override
    public List<ICoreHandler> getCoreHandlersFrom(EntityPlayer player){
        return ((List<ItemStack>) player.getArmorInventoryList())
                .stream()
                .filter(ExoskeletonTag::hasStackTag)
                .map(ExoskeletonTag::getStackTag)
                .filter(Optional::isPresent)
                .map((opt)->{
                    NBTTagCompound tag = opt.get();
                    if(!tag.hasKey("Core")) return Optional.<ICoreHandler>empty();
                    tag = tag.getCompoundTag("Core");
                    return getCoreHandlerFor(tag.getString("Name"));
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public void registerSkillTree(String tag, SkillTree tree, ICoreHandler handler){
        CoreRegistry.register(tag, tree, handler);
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