package Ex1.Inheritance;

import ast.AstNode;
import ast.ClassDecl;
import ast.Program;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InheritanceTrees {
    private HashMap<String, InheritanceNode> roots;
    private HashMap<String, InheritanceNode> flatClasses;

    public InheritanceTrees(Program prog){
        InheritanceNode currNode;
        this.roots = new HashMap<>();
        this.flatClasses = new HashMap<>();
        if (prog.classDecls() != null){
            for (ClassDecl classdecl : prog.classDecls()){
                if (classdecl.superName() == null){
                    currNode = new InheritanceNode(null, classdecl);
                    roots.put(classdecl.name(), currNode);
                    flatClasses.put(classdecl.name(), currNode);
                }

                else{
                    InheritanceNode parent = flatClasses.get(classdecl.superName());
                    if (parent != null){
                        currNode = new InheritanceNode(parent, classdecl);
                        parent.addToChildren(currNode);
                        flatClasses.put(classdecl.name(), currNode);
                    }
                }
            }
        }
    }

//    //public HashMap<String, InheritanceNode> roots(){
//        return this.roots;
//    }

//    public HashMap<String, InheritanceNode> flatClasses(){
//        return this.flatClasses;
//    }

    public InheritanceNode className2InheritanceNode(String className){
        return this.flatClasses.get(className);
    }

    public InheritanceNode classAstNode2InheritanceNode(AstNode classAstNode){
        ClassDecl classDecl = (ClassDecl)classAstNode;
        String classId = classDecl.name();
        return className2InheritanceNode(classId);
    }


    public Set<String> GetAllClassesUnderAncestor(InheritanceNode highestAncestor) {
        Set<String> classesToCheck = new HashSet<>();
        String name = highestAncestor.name();
        classesToCheck.add(name);

        if (!(highestAncestor.hasChildren())) {
            return classesToCheck;
        }

        for (InheritanceNode child : highestAncestor.children()) {
            classesToCheck.addAll(GetAllClassesUnderAncestor(child));
        }

        return classesToCheck;
    }

    public HashMap<String, InheritanceNode> roots(){
        return this.roots;
    }

    public HashMap<String, InheritanceNode> getFlatClasses(){
        return flatClasses;
    }



//
//    public InheritanceNode currNode(){
//        return this.currNode;
//    }



}
