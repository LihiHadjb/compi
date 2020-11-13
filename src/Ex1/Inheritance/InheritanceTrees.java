package Ex1.Inheritance;

import Ex1.Inheritance.InheritanceNode;
import ast.ClassDecl;
import ast.Program;
import java.util.HashMap;

public class InheritanceTrees {
    HashMap<String, InheritanceNode> roots;
    HashMap<String, InheritanceNode> flatClasses;
    InheritanceNode currNode;

    public InheritanceTrees(Program prog){
        this.roots = new HashMap<>();
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

    public HashMap<String, InheritanceNode> flatClasses(){
        return this.flatClasses;
    }

    public InheritanceNode currNode(){
        return this.currNode;
    }



}
