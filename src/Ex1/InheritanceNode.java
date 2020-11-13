package Ex1;

import ast.AstNode;
import ast.ClassDecl;

import java.util.ArrayList;
import java.util.List;

public class InheritanceNode {
    InheritanceNode parent;
    List<InheritanceNode> children;
    String name;
    AstNode astNode;

    public InheritanceNode(InheritanceNode parent, ClassDecl classDecl){
        this.parent = parent;
        this.children = new ArrayList<>();
        this.name = classDecl.name();
        this.astNode = classDecl;

    }

    public void addToChildren(InheritanceNode newChild){
        this.children.add(newChild);
    }

    public String toString(){
        return "parent: " + parent.toString() + "\n" +
                "children: " + children.toString() + "\n" +
                "name: " + name + "\n";
    }


}
