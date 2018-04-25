package exoskeleton.common.skill;

import exoskeleton.api.skill.SkillTreeNode;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

final class SkillTreeNodeView{
    public static final Dimension SIZE = new Dimension(32, 32);

    private final Point location = new Point(0, 0);
    private final SkillTreeNode node;

    SkillTreeNodeView(SkillTreeNode node){
        this.node = node;
    }

    public SkillTreeNode getNode(){
        return this.node;
    }

    public int getX(){
        return (int) this.location.getX();
    }

    public int getY(){
        return (int) (this.location.getY());
    }

    public Point getPosition(){
        return this.location;
    }

    public void setPosition(Point p){
        this.location.setLocation(p);
    }

    public void move(int x, int y){
        this.location.setLocation(x, y);
    }

    public void translate(int dX, int dY){
        this.location.translate(dX, dY);
    }

    public boolean intersectedBy(Point p){
        Rectangle bounds = new Rectangle(this.getX(), this.getY(), (int) SIZE.getWidth(), (int) SIZE.getHeight());
        return bounds.contains(new Rectangle((int) p.getX(), (int) p.getY(), (int) SIZE.getWidth(), (int) SIZE.getHeight()));
    }

    public Point offsetBy(SkillTreeNode.Direction dir, int spacing){
        int x = this.getX();
        int y = this.getY();
        switch(dir){
            case NORTH:
                y -= (SIZE.getHeight() * spacing * 2);
                break;
            case SOUTH:
                y += (SIZE.getHeight() * spacing * 2);
                break;
            case WEST:
                x -= (SIZE.getWidth() * spacing * 2);
                break;
            case EAST:
                x += (SIZE.getWidth() * spacing * 2);
                break;
            default: break;
        }

        return new Point(x, y);
    }
}