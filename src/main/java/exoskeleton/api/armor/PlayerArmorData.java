package exoskeleton.api.armor;

import com.google.common.collect.ImmutableSet;
import exoskeleton.api.IExoskeletonApi;
import exoskeleton.api.skill.SkillTree;
import exoskeleton.api.skill.SkillTreeNode;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class PlayerArmorData{
    private final EntityPlayer player;
    private final PlayerArmorSlot slot;
    private final float[] stats = new float[PlayerArmorStat.values().length];
    private final SkillTree skillTree;
    private final Set<SkillTreeNode> unlockedSkills = new HashSet<>();

    public PlayerArmorData(EntityPlayer player, NBTTagCompound c){
        try{
            this.player = player;
            this.slot = PlayerArmorSlot.values()[c.getByte("Slot")];
            this.skillTree = IExoskeletonApi.getInstance()
                    .getSkillTreeFor(c.getString("SkillTree"))
                    .orElseThrow(()->new IllegalStateException("Cannot load skill tree for armor"));
            for(PlayerArmorStat stat : PlayerArmorStat.values()){
                this.stats[stat.ordinal()] = c.getFloat(stat.name() + "Stat");
            }

            NBTTagList tagList = c.getTagList("Unlocked", 0xA);
            for(int i = 0; i < tagList.tagCount(); i++){
                NBTTagCompound tagComp = tagList.getCompoundTagAt(i);
                Optional<SkillTreeNode> nodeOpt = this.skillTree.findNode(tagComp.getString("Name"));
                nodeOpt.ifPresent(this.unlockedSkills::add);
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public SkillTree getSkillTree(){
        return this.skillTree;
    }

    public Set<SkillTreeNode> getUnlockedSkills(){
        return ImmutableSet.copyOf(this.unlockedSkills);
    }

    public boolean isSkillUnlocked(String name){
        return this.unlockedSkills.stream()
                .anyMatch((n)->n.getTag().equals(name));
    }

    public EntityPlayer getPlayer(){
        return this.player;
    }

    public float getStat(PlayerArmorStat stat){
        return this.stats[stat.ordinal()];
    }

    public void setStat(PlayerArmorStat stat, float value){
        this.stats[stat.ordinal()] = value;
    }

    public void save(NBTTagCompound c){
        c.setByte("Slot", (byte) this.slot.ordinal());
        for(PlayerArmorStat stat : PlayerArmorStat.values()){
            c.setFloat(stat.name() + "Stat", this.stats[stat.ordinal()]);
        }
    }
}