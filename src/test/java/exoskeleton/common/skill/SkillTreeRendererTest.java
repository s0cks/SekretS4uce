package exoskeleton.common.skill;

import exoskeleton.api.skill.SkillTree;
import exoskeleton.api.skill.SkillTreeNode;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import org.junit.Test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class SkillTreeRendererTest{
    @Test
    public void testRender() throws Exception{
        SkillTree tree = new SkillTree("Mystic");

        SkillTreeNode root1 = new SkillTreeNode("Root1");
        SkillTreeNode root2 = new SkillTreeNode("Root2");

        tree.addRoot(root1);
        tree.addRoot(root2);

        SkillTreeNode branch1a = new SkillTreeNode("Branch 1 A", root1);
        SkillTreeNode branch1b = new SkillTreeNode("Branch 1 B", root2);
        SkillTreeNode leaf2 = new SkillTreeNode("Leaf 1", branch1a, SkillTreeNode.Direction.NORTH);

        SkillTreeNode leaf3 = new SkillTreeNode("Join 1", branch1a)
                .addParent(branch1b);

        SkillTreeNode leaf4 = new SkillTreeNode("Leaf 2", branch1b);

        System.out.println("Branch 1A: " + Arrays.toString(branch1a.getChildren()));
        System.out.println("Branch 1B: " + Arrays.toString(branch1b.getChildren()));

        Path image = Paths.get(System.getProperty("user.home"), "Desktop", "output.jpg");
        Dimension size = new Dimension(32 * 10, 32 * 10);

        SkillTreeLayouter layouter = new SkillTreeLayouter(tree, size);
        tree.accept(layouter);

        SkillTreeRenderer renderer = new SkillTreeRenderer(layouter);
        tree.accept(renderer);

        if(Files.exists(image)){
            Files.delete(image);
        }

        try(OutputStream out = Files.newOutputStream(image, StandardOpenOption.CREATE, StandardOpenOption.WRITE)){
            ImageIO.write(renderer.getImage(), "jpg", out);
            out.flush();
        }
    }
}