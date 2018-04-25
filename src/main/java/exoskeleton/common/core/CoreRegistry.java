package exoskeleton.common.core;

import exoskeleton.api.core.ICoreHandler;
import exoskeleton.api.skill.SkillTree;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CoreRegistry{
    private CoreRegistry(){}

    private static final Map<String, CoreRegistration> coreMap = new HashMap<>();
    static{

    }

    public static void register(String tag, SkillTree tree, ICoreHandler handler){
        if(coreMap.containsKey(tag)) return;
        coreMap.put(tag, new CoreRegistration(tree, handler));
    }

    private static Optional<CoreRegistration> getCoreRegistrationFor(String tag){
        return Optional.ofNullable(coreMap.getOrDefault(tag, null));
    }

    public static Optional<ICoreHandler> getCoreHandlerFor(String tag){
        Optional<CoreRegistration> coreReg = getCoreRegistrationFor(tag);
        return coreReg.isPresent() ?
                Optional.ofNullable(coreReg.get().coreHandler) :
                Optional.empty();
    }

    public static Optional<SkillTree> getSkillTreeFor(String tag){
        Optional<CoreRegistration> coreReg = getCoreRegistrationFor(tag);
        return coreReg.isPresent() ?
                Optional.ofNullable(coreReg.get().skillTree) :
                Optional.empty();
    }

    private static final class CoreRegistration{
        private final SkillTree skillTree;
        private final ICoreHandler coreHandler;

        public CoreRegistration(SkillTree skillTree, ICoreHandler coreHandler){
            this.skillTree = skillTree;
            this.coreHandler = coreHandler;
        }
    }
}