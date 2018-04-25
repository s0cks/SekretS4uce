package exoskeleton.common.skill;

import exoskeleton.api.skill.SkillTree;
import exoskeleton.api.skill.SkillTreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

final class SkillTreeRenderer
extends AbstractSkillTreeVisitor{
    private final Logger logger = LoggerFactory.getLogger(SkillTreeRenderer.class.getSimpleName());
    private final SkillTreeView treeView;
    private final Map<SkillTreeNode, SkillTreeNodeView> nodeViews;
    private final BufferedImage image;

    SkillTreeRenderer(SkillTreeView view, Map<SkillTreeNode, SkillTreeNodeView> nodeViews){
        super(view.getOwner());
        this.treeView = view;
        this.nodeViews = nodeViews;
        this.image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    SkillTreeRenderer(SkillTreeLayouter layouter){
        this(layouter.getTreeView(), layouter.getNodeViews());
    }

    public BufferedImage getImage(){
        return this.image;
    }

    private Rectangle getDrawGeometry(){
        return this.treeView.getDrawableGeometry();
    }

    @Override
    public void visitTree(SkillTree tree){
        this.logger.info("Rendering tree with size " + this.treeView.getWidth() + "x" + this.treeView.getHeight());

        Graphics2D g2 = (Graphics2D) this.image.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, this.treeView.getWidth(), this.treeView.getHeight());

        LineRenderer lineRenderer = new LineRenderer(this.getOwner(), this.nodeViews, g2)
                .setColor(Color.BLACK)
                .setStroke(new BasicStroke(2));
        this.getOwner().accept(lineRenderer);

        NodeRenderer nodeRenderer = new NodeRenderer(this.getOwner(), this.nodeViews, g2)
                .setColor(Color.CYAN);
        this.getOwner().accept(nodeRenderer);
    }

    @Override
    public void visitNode(SkillTreeNode node){

    }

    private static final class NodeRenderer
    extends AbstractSkillTreeVisitor{
        private final Map<SkillTreeNode, SkillTreeNodeView> viewMap;
        private final Graphics2D g2;
        private Color color = Color.WHITE;

        NodeRenderer(SkillTree owner, Map<SkillTreeNode, SkillTreeNodeView> viewMap, Graphics2D g2) {
            super(owner);
            this.viewMap = viewMap;
            this.g2 = g2;
        }

        NodeRenderer setColor(Color color){
            this.color = color;
            return this;
        }

        void drawLabel(SkillTreeNode node, int x, int y, Dimension size){
            int width = this.g2.getFontMetrics().stringWidth(node.getTag());

            int lX = x + ((int) size.getWidth() - width) / 2;
            int lY = y + this.g2.getFontMetrics().getHeight();

            Color c = this.g2.getColor();
            this.g2.setColor(Color.BLACK);
            this.g2.drawString(node.getTag(), lX, lY);
            this.g2.setColor(c);
        }

        @Override
        public void visitTree(SkillTree tree){
            this.g2.setColor(this.color);
            Arrays.asList(tree.getRoots())
                    .forEach((n)->n.accept(this));
        }

        @Override
        public void visitNode(SkillTreeNode node){
            Point pos = this.viewMap.get(node).getPosition();
            Dimension size = SkillTreeNodeView.SIZE;

            int x = (int) (pos.getX() - (size.getWidth() / 2));
            int y = (int) (pos.getY() - (size.getHeight() / 2));

            this.g2.fillRect(x, y, (int) size.getWidth(), (int) size.getHeight());
            this.drawLabel(node, x, y, size);
        }
    }

    private static final class LineRenderer
    extends AbstractSkillTreeVisitor{
        private final Logger logger = LoggerFactory.getLogger(LineRenderer.class.getSimpleName());
        private final Queue<SkillTreeNode> nodeQueue = new LinkedList<>();
        private final Map<SkillTreeNode, SkillTreeNodeView> viewMap;
        private final Graphics2D g2;
        private SkillTreeNode parent;

        LineRenderer(SkillTree owner, Map<SkillTreeNode, SkillTreeNodeView> viewMap, Graphics2D g2) {
            super(owner);
            this.viewMap = viewMap;
            this.g2 = g2;
        }

        LineRenderer setColor(Color color){
            this.g2.setColor(color);
            return this;
        }

        LineRenderer setStroke(Stroke stroke){
            this.g2.setStroke(stroke);
            return this;
        }

        void drawLineTo(Point parentPos, Point nodePos){
            this.g2.drawLine(parentPos.x, parentPos.y, nodePos.x, parentPos.y);
            this.g2.drawLine(nodePos.x, parentPos.y, nodePos.x, nodePos.y);
        }

        @Override
        public void visitTree(SkillTree tree){
            this.logger.info("Visiting Skill Tree: " + tree.getTag());
            this.nodeQueue.addAll(Arrays.asList(tree.getRoots()));
            while((this.parent = this.nodeQueue.poll()) != null){
                this.parent.accept(this);
            }
        }

        @Override
        public void visitNode(SkillTreeNode node){
            if(node.isRoot()) return;

            this.logger.info("Visiting Skill Tree Node: " + node.getTag());
            SkillTreeNodeView nodeView = this.viewMap.get(node);
            SkillTreeNodeView parentView = this.viewMap.get(this.parent);
            Point nodePos = nodeView.getPosition();
            Point parentPos = parentView.getPosition();

            this.logger.info("Drawing line from " + parentView.getNode().getTag() + " to " + nodeView.getNode().getTag());
            this.drawLineTo(parentPos, nodePos);

            if(node.getParent() != null && !this.viewMap.containsKey(node.getParent())){
                this.nodeQueue.add(node.getParent());
            }
        }
    }
}