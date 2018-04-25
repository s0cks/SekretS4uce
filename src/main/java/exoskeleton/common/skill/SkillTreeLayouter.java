package exoskeleton.common.skill;

import com.google.common.collect.ImmutableMap;
import exoskeleton.api.skill.SkillTree;
import exoskeleton.api.skill.SkillTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.actors.threadpool.Arrays;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

final class SkillTreeLayouter
extends AbstractSkillTreeVisitor{
    private final Logger logger = LoggerFactory.getLogger(SkillTreeLayouter.class.getSimpleName());
    private final SkillTreeView view;
    private final Map<SkillTreeNode, SkillTreeNodeView> nodeViews = new HashMap<>();
    private final Queue<SkillTreeNode> nodeQueue = new LinkedList<>();
    private SkillTreeNode lastRoot;

    SkillTreeLayouter(SkillTree owner, Dimension size){
        super(owner);
        this.view = new SkillTreeView(owner, size);
    }

    private Point getOrigin(){
        return this.view.getOrigin();
    }

    private Point getRelativePositionTo(SkillTreeNode from, SkillTreeNode to){
        SkillTreeNodeView fromView = this.nodeViews.getOrDefault(from, null);
        if(fromView == null) return this.getOrigin();
        return fromView.offsetBy(SkillTreeNode.Direction.SOUTH, 1);
    }

    public Map<SkillTreeNode, SkillTreeNodeView> getNodeViews(){
        return ImmutableMap.copyOf(this.nodeViews);
    }

    public SkillTreeView getTreeView(){
        return this.view;
    }

    @Override
    public void visitTree(SkillTree tree){
        this.logger.info("Visiting Skill Tree: " + tree.getTag());

        this.nodeQueue.addAll(Arrays.asList(tree.getRoots()));
        SkillTreeNode name;
        while((name = this.nodeQueue.poll()) != null){
            name.accept(this);
            this.lastRoot = name;
        }
    }

    @Override
    public void visitNode(SkillTreeNode node){
        if(this.nodeViews.containsKey(node)) return;

        this.logger.info("Visiting Skill Tree Node: " + node.getTag());
        SkillTreeNodeView nodeView = new SkillTreeNodeView(node);
        if(node.isRoot()){
            this.nodeViews.put(node, nodeView);
            Point p = this.lastRoot != null ?
                    this.getRelativePositionTo(this.lastRoot, node) :
                    this.getOrigin();
            nodeView.setPosition(p);

            this.logger.info("Drawing Node @ " + nodeView.getPosition());
        } else{
            SkillTreeNode parent = node.getParent();
            if(!this.nodeViews.containsKey(parent)){
                this.nodeQueue.add(parent);
                this.nodeQueue.add(node);
                this.logger.info("Adding parent: " + parent.getTag());
                return;
            }

            SkillTreeNodeView parentView = this.nodeViews.get(parent);
            nodeView.setPosition(parentView.offsetBy(node.getDirection(), 1));

            if(parent.getChildren().length > 1){
                for(SkillTreeNode other : parent.getChildren()){
                    SkillTreeNodeView otherView = this.nodeViews.getOrDefault(other, null);
                    if(otherView == null) continue;
                    if(otherView.getX() == nodeView.getX()){
                        nodeView.setPosition(new Point(nodeView.getX() + (32 * 2), nodeView.getY()));
                    }
                }
            }

            this.logger.info("Drawing Node @ " + nodeView.getPosition());
            this.nodeViews.put(node, nodeView);
        }
    }
}