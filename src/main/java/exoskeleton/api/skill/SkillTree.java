package exoskeleton.api.skill;

import java.util.LinkedList;
import java.util.List;

public final class SkillTree{
    private final String tag;
    private final List<SkillTreeNode> roots;

    public SkillTree(String tag){
        this.tag = tag;
        this.roots = new LinkedList<>();
    }

    public void addRoot(SkillTreeNode root){
        this.roots.add(root);
    }

    public String getTag(){
        return this.tag;
    }

    public SkillTreeNode[] getRoots(){
        return this.roots.toArray(new SkillTreeNode[this.roots.size()]);
    }

    public void accept(SkillTreeVisitor vis){
        vis.visitTree(this);
    }
}