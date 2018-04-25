package exoskeleton.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Optional;

public final class ExoskeletonTag{
    private static final String IDENTIFIER = "Exoskeleton";

    private ExoskeletonTag(){}

    public static Optional<NBTTagCompound> getStackTag(ItemStack stack){
        if(stack == null) return Optional.empty();
        if(!(stack.hasTagCompound())) stack.setTagCompound(new NBTTagCompound());
        NBTTagCompound comp = stack.getTagCompound();
        if(!(comp.hasKey(IDENTIFIER))) comp.setTag(IDENTIFIER, new NBTTagCompound());
        return Optional.ofNullable(comp.getCompoundTag(IDENTIFIER));
    }
}