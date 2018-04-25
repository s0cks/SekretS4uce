package exoskeleton.common.skill;

import exoskeleton.api.skill.SkillTree;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

final class SkillTreeView{
    private final SkillTree owner;
    private final Dimension size;
    private final Insets insets;

    SkillTreeView(SkillTree owner, Dimension size){
        this.owner = owner;
        this.size = size;
        this.insets = new Insets(128, 128, 128, 128);
    }

    public Insets getInsets(){
        return this.insets;
    }

    public Dimension getSize(){
        return this.size;
    }

    public SkillTree getOwner(){
        return this.owner;
    }

    public Point getOrigin(){
        return new Point(this.insets.left, this.insets.top);
    }

    public int getMinX(){
        return this.insets.left;
    }

    public int getMaxX(){
        return (int) this.size.getWidth() - this.insets.left - this.insets.right;
    }

    public int getMinY(){
        return this.insets.top;
    }

    public int getMaxY(){
        return (int) this.size.getHeight() - this.insets.top - this.insets.bottom;
    }

    public int getWidth(){
        return (int) this.size.getWidth();
    }

    public int getHeight(){
        return (int) this.size.getHeight();
    }

    public Rectangle getDrawableGeometry(){
        int width = this.getMaxX() - this.getMinX();
        int height = this.getMaxY() - this.getMinY();
        return new Rectangle(0, 0, width, height);
    }
}