package exoskeleton.api;

import exoskeleton.api.skill.SkillTree;

import java.lang.reflect.Field;
import java.util.Optional;

public interface IExoskeletonApi{
    SkillTree getSkillTree(String tag);
    void registerSkillTree(String tag, SkillTree tree);

    @SuppressWarnings("unchecked")
    static Optional<IExoskeletonApi> getInstance(){
        Class<? extends IExoskeletonApi> apiCls;
        try{
            apiCls = (Class<? extends IExoskeletonApi>) Class.forName("exoskeleton.common.Exoskeleton");
            Field instanceField = apiCls.getDeclaredField("instance");
            return Optional.ofNullable((IExoskeletonApi) instanceField.get(null));
        } catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException e){
            return Optional.empty();
        }
    }
}