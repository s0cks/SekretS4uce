package exoskeleton.api;

import exoskeleton.api.armor.PlayerArmorData;
import exoskeleton.api.armor.PlayerArmorSlot;
import exoskeleton.api.core.ICoreHandler;
import exoskeleton.api.skill.SkillTree;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public interface IExoskeletonApi{
    Optional<SkillTree> getSkillTreeFor(String tag);
    Optional<ICoreHandler> getCoreHandlerFor(String tag);
    Optional<ICoreHandler> getCoreHandlerFor(ItemStack stack);
    Optional<PlayerArmorData> getPlayerArmorData(EntityPlayer player, PlayerArmorSlot slot);

    @Deprecated
    List<ICoreHandler> getCoreHandlersFrom(EntityPlayer player);

    void registerSkillTree(String tag, SkillTree tree, ICoreHandler handler);

    static IExoskeletonApi getInstance(){
        return getInstanceSafely()
                .orElseGet(()->null);
    }

    @SuppressWarnings("unchecked")
    static Optional<IExoskeletonApi> getInstanceSafely(){
        Class<? extends IExoskeletonApi> apiCls;
        try{
            apiCls = (Class<? extends IExoskeletonApi>) Class.forName("exoskeleton.common.Exoskeleton");
            Field instanceField = apiCls.getDeclaredField("instance");
            return Optional.ofNullable((IExoskeletonApi) instanceField.get(null));
        } catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException e){
            return Optional.empty();
        }
    }
}