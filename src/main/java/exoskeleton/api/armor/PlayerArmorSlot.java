package exoskeleton.api.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public enum PlayerArmorSlot{
    HEAD(EntityEquipmentSlot.HEAD),
    CHEST(EntityEquipmentSlot.CHEST),
    LEGGINGS(EntityEquipmentSlot.LEGS),
    BOOTS(EntityEquipmentSlot.FEET);

    private final EntityEquipmentSlot equipSlot;

    PlayerArmorSlot(EntityEquipmentSlot equipSlot){
        this.equipSlot = equipSlot;
    }

    public static Predicate<ItemStack> isSlot(PlayerArmorSlot slot){
        return new SlotPredicate(slot);
    }

    private static final class SlotPredicate
    implements Predicate<ItemStack>{
        private final PlayerArmorSlot targetSlot;
        
        SlotPredicate(PlayerArmorSlot targetSlot){
            this.targetSlot = targetSlot;
        }
        
        @Override
        public boolean test(ItemStack itemStack){
            return (itemStack.getItem() instanceof ItemArmor) &&
                    ((ItemArmor) itemStack.getItem()).getEquipmentSlot().equals(this.targetSlot.equipSlot);
        }
    }
}