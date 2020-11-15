package Ex1.SymbolTables;

import ast.AstNode;
import ast.MethodDecl;
import ast.VariableIntroduction;

import java.util.HashMap;

public class SymbolTable {
    String type; //"class" or "method"
    HashMap<String, MethodEntry> methods;
    HashMap<String, VarEntry> variables;
    SymbolTable parent;
    AstNode astNodeInProgram;

    public SymbolTable(SymbolTable parent, AstNode astNode, String type){
        this.type = type;
        this.methods = new HashMap<String, MethodEntry>();
        this.variables = new HashMap<String, VarEntry>();
        this.parent = parent;
        this.astNodeInProgram = astNode;
    }

    public void addVarEntry(VariableIntroduction var){
        VarEntry entry = new VarEntry(var);
        String name = var.name();
        this.variables.put(name, entry);
    }

    public void addMethodEntry(MethodDecl methodDecl){
        MethodEntry entry = new MethodEntry();
        String name = methodDecl.name();
        this.methods.put(name, entry);
    }

    public String type(){
        return this.type;
    }

    public HashMap<String, MethodEntry> methods(){
        return this.methods;
    }

    public HashMap<String, VarEntry> variables(){
        return this.variables;
    }

    public SymbolTable parent(){
        return this.parent;
    }

    public AstNode astNodeInProgram(){
        return this.astNodeInProgram;
    }

    public String getVariableType(MethodlDecl lastMethodSeen, String varName){
//        for(VarEntry varEntry : lastMethodSeen.symbolTable().variables()){
//            if varEntry.
//        }
        if(lastMethodSeen.symbolTable().variables().keySet().contains(varName)){
            return lastMethodSeen.symbolTable().variables().get(varName).
        }
    }
}
