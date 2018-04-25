package exoskeleton.common.core;

import exoskeleton.api.core.ICoreHandler;
import exoskeleton.api.skill.SkillTree;
import exoskeleton.common.Exoskeleton;
import exoskeleton.common.core.handlers.CoreHandlerRecon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CoreRegistry{
    private CoreRegistry(){}

    private static final Map<String, CoreRegistration> coreMap = new HashMap<>();
    static{
        try{
            register("recon", new CoreHandlerRecon());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private static SkillTree loadSkillTree(String name) throws Exception{
        String pth = "/assets/exoskeleton/trees/" + name + ".json";
        try(InputStream in = System.class.getResourceAsStream(pth)){
            return Exoskeleton.gson.fromJson(new InputStreamReader(in), SkillTree.class);
        }
    }

    private static void register(String tag, ICoreHandler handler) throws Exception{
        register(tag, loadSkillTree(tag), handler);
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

        CoreRegistration(SkillTree skillTree, ICoreHandler coreHandler){
            this.skillTree = skillTree;
            this.coreHandler = coreHandler;
        }
    }
}