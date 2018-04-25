package exoskeleton.api.skill;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public final class SkillTreeNode{
    private final String tag;
    private final SkillTreeNode parent;
    private final List<SkillTreeNode> children;
    private final Direction direction;

    public SkillTreeNode(String tag, SkillTreeNode parent, Direction dir){
        this.tag = tag;
        this.parent = parent;
        this.direction = dir;
        this.children = new LinkedList<>();
        if(parent != null) parent.children.add(this);
    }

    public SkillTreeNode(String tag, SkillTreeNode parent){
        this(tag, parent, parent == null ? Direction.SOUTH : Direction.EAST);
    }

    public SkillTreeNode(String tag, Direction dir){
        this(tag, null, dir);
    }

    public SkillTreeNode(String tag){
        this(tag, null, Direction.SOUTH);
    }

    public Direction getDirection(){
        return this.direction;
    }

    public String getTag(){
        return this.tag;
    }

    public SkillTreeNode getParent(){
        return this.parent;
    }

    public SkillTreeNode[] getChildren(){
        return this.children.toArray(new SkillTreeNode[this.children.size()]);
    }

    public boolean isRoot(){
        return this.children.size() > 0 && this.parent == null;
    }

    public SkillTreeNode addParent(SkillTreeNode parent){
        parent.children.add(this);
        return this;
    }

    public boolean isLeaf(){
        return this.children.isEmpty();
    }

    public void accept(SkillTreeVisitor vis){
        vis.visitNode(this);
        if(!this.isLeaf()){
            this.children.forEach((c)->c.accept(vis));
        }
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SkillTreeNode)) return false;
        SkillTreeNode node = (SkillTreeNode) obj;
        return this.getTag().equals(node.getTag()) &&
                Objects.equals(this.getParent(), node.getParent()) &&
                this.getDirection().equals(node.getDirection()) &&
                Arrays.equals(this.getChildren(), node.getChildren());
    }

    @Override
    public int hashCode(){
        int hash = 31;
        hash = 37 * hash + this.tag.hashCode();
        hash = 37 * hash + this.direction.hashCode();
        if(this.parent != null) hash = 37 * hash + this.parent.hashCode();
        if(!this.children.isEmpty()) hash = 37 * hash + this.children.size();
        return hash;
    }

    @Override
    public String toString() {
        return "SkillTreeNode{" +
                "tag=" + this.tag + ";" +
                "parent=" + (this.parent == null ? "null" : this.parent.getTag()) + ";" +
                "children=" + Arrays.toString(this.getChildren()) + ";" +
                "direction=" + this.direction + "}";
    }

    public enum Direction{
        NORTH, SOUTH,
        EAST, WEST;
    }
}