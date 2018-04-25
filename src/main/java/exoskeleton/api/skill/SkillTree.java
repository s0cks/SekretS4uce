package exoskeleton.api.skill;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public final class SkillTree{
    private final String tag;
    private final List<SkillTreeNode> roots;

    public SkillTree(String tag){
        this.tag = tag;
        this.roots = new LinkedList<>();
    }

    public Optional<SkillTreeNode> findNode(String name){
        SkillTreeNodeFinder finder = new SkillTreeNodeFinder(name);
        this.accept(finder);
        return finder.getResult();
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

    private static final class SkillTreeNodeFinder
    implements SkillTreeVisitor{
        private final String target;
        private SkillTreeNode result;

        SkillTreeNodeFinder(String target){
            this.target = target;
        }

        public Optional<SkillTreeNode> getResult(){
            return Optional.ofNullable(this.result);
        }

        @Override
        public void visitTree(SkillTree tree){
            Queue<SkillTreeNode> nodeQueue = new LinkedList<>(Arrays.asList(tree.getRoots()));

            SkillTreeNode node;
            while((node = nodeQueue.poll()) != null && this.result == null){
                node.accept(this);
            }
        }

        @Override
        public void visitNode(SkillTreeNode node){
            if(node.getTag().equals(this.target)){
                this.result = node;
                return;
            }

            if(!node.isLeaf()){
                Arrays.stream(node.getChildren())
                        .forEach((n)->n.accept(this));
            }
        }
    }
}