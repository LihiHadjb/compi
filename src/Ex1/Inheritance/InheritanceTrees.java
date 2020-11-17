package Ex1.Inheritance;

import Ex1.Inheritance.InheritanceNode;
import ast.ClassDecl;
import ast.Program;
import java.util.HashMap;

public class InheritanceTrees {
    private HashMap<String, InheritanceNode> roots;
    private HashMap<String, InheritanceNode> flatClasses;

    public InheritanceTrees(Program prog){
        InheritanceNode currNode;
        this.roots = new HashMap<>();
        this.flatClasses = new HashMap<>();
        for (ClassDecl classdecl : prog.classDecls()){
            if (classdecl.superName() == null){
                currNode = new InheritanceNode(null, classdecl);
                roots.put(classdecl.name(), currNode);
            }

            else{
                InheritanceNode parent = flatClasses.get(classdecl.superName());
                currNode = new InheritanceNode(parent, classdecl);
                parent.addToChildren(currNode);
            }
            flatClasses.put(classdecl.name(), currNode);
        }
    }

    public HashMap<String, InheritanceNode> roots(){
        return this.roots;
    }

//    public HashMap<String, InheritanceNode> flatClasses(){
//        return this.flatClasses;
//    }

    public InheritanceNode getInheritanceNodeOfClassName(String className){
        return this.flatClasses.get(className);
    }

    public InheritanceNode getInheritanceNodeOfAstNode(ClassDecl classAstNode){
        String classId = classAstNode.name();
        return getInheritanceNodeOfClassName(classId);
    }



//
//    public InheritanceNode currNode(){
//        return this.currNode;
//    }



}
