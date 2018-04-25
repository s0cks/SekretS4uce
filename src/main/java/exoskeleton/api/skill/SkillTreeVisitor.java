package exoskeleton.api.skill;

public interface SkillTreeVisitor{
    void visitTree(SkillTree tree);
    void visitNode(SkillTreeNode node);
}