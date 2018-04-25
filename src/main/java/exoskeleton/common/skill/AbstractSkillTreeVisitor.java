package exoskeleton.common.skill;

import exoskeleton.api.skill.SkillTree;
import exoskeleton.api.skill.SkillTreeVisitor;

abstract class AbstractSkillTreeVisitor
implements SkillTreeVisitor{
    protected final SkillTree owner;

    AbstractSkillTreeVisitor(SkillTree owner){
        this.owner = owner;
    }

    public SkillTree getOwner(){
        return this.owner;
    }
}
