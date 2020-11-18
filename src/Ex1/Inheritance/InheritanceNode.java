package Ex1.Inheritance;

import ast.AstNode;
import ast.ClassDecl;

import java.util.ArrayList;
import java.util.List;

public class InheritanceNode {
    private InheritanceNode parent;
    private List<InheritanceNode> children;
    private String name;
    private AstNode astNode;

    public InheritanceNode(InheritanceNode parent, ClassDecl classDecl){
        this.parent = parent;
        this.children = new ArrayList<>();
        this.name = classDecl.name();
        this.astNode = classDecl;

    }

    public void addToChildren(InheritanceNode newChild){
        this.children.add(newChild);
    }

    public String name(){
        return this.name;
    }

    public InheritanceNode parent(){ return this.parent; }

    public AstNode astNode(){ return this.astNode; }

    public List<InheritanceNode> children(){
        return this.children;
    }

    public boolean hasChildren(){
        return !this.children.isEmpty();
    }


}
