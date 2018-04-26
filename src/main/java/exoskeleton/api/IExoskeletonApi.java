package exoskeleton.api;

import exoskeleton.api.armor.PlayerArmorData;
import exoskeleton.api.armor.PlayerArmorSlot;
import exoskeleton.api.core.ICoreHandler;
import exoskeleton.api.skill.SkillTree;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * # Exoskeleton API
 *
 * Please Note:
 *
 * 1.) A lot of these functions return Java8's {@link Optional} type. It will remain given that this mod,
 * will only work on Java8. Please use with caution.
 *
 * 2.) Usage of {@link Optional} is subject to change.
 *
 * @author s0cks
 */
public interface IExoskeletonApi{
    /**
     * This will take a Json document (represented by an {@link InputStream})
     * and convert it to a {@link SkillTree}.
     *
     * Please note:
     *
     * 1.) You can construct your own {@link SkillTree} as well via whatever means you would like.
     * This is just merely a convenience method to help people rapidly prototype additions to Exo
     *
     * 2.) This function does not return an {@link Optional} type because it's expected that the
     * {@link InputStream} represents a {@link SkillTree} properly.
     *
     * 3.) In the future this may return an {@link Optional} type, or this also may throw
     * an {@link Exception} upon failing to load the tree properly.
     *
     * @see SkillTree
     *
     * @author s0cks
     * @param input - Json document as an {@link InputStream}
     * @return The parsed and constructed {@link SkillTree}
     */
    SkillTree load(InputStream input);

    /**
     * Function for providing addon mods with any skill tree registered in Exoskeleton
     *
     * @see SkillTree
     *
     * @author s0cks
     * @param tag This is the name of the skill tree
     * @return The skill tree that was found in the registrar
     */
    Optional<SkillTree> getSkillTreeFor(String tag);

    /**
     * Function for providing an {@link ICoreHandler} instance for a given {@link SkillTree}
     * This function is essentially duplicate to {@link IExoskeletonApi#getCoreHandlerFor(ItemStack)},
     * the key difference being that this accepts a String and is therefor optimal to call.
     *
     * @see SkillTree
     * @see ICoreHandler
     * @see #getCoreHandlerFor(ItemStack)
     *
     * @author s0cks
     * @param tag - The name of the {@link SkillTree}
     * @return An {@link Optional} value for the expected {@link ICoreHandler}
     */
    Optional<ICoreHandler> getCoreHandlerFor(String tag);

    /**
     * Function for providing an {@link ICoreHandler} instance for the given {@link ItemStack}.
     *
     * Please Note:
     *
     * This function is a less optimal call compared to {@link #getCoreHandlerFor(String)} please use with caution.
     *
     * @see SkillTree
     * @see ICoreHandler
     * @see #getCoreHandlerFor(String)
     *
     * @author s0cks
     * @param stack The {@link ItemStack} that contains the proper {@link net.minecraft.nbt.NBTTagCompound} to get the {@link ICoreHandler}
     * @return An {@link Optional} value for the expected {@link ICoreHandler}
     */
    Optional<ICoreHandler> getCoreHandlerFor(ItemStack stack);

    Optional<PlayerArmorData> getPlayerArmorData(EntityPlayer player, PlayerArmorSlot slot);
    Optional<PlayerArmorData> getPlayerArmorData(EntityPlayer player, ItemStack stack);

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