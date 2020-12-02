package Ex1.SymbolTables;

import ast.*;

import java.util.HashMap;

public class SymbolTable {
    private String type; //"class" or "method"
    private HashMap<String, MethodEntry> methods;
    private HashMap<String, VarEntry> variables;
    private SymbolTable parent;
    private AstNode astNodeInProgram;

    public SymbolTable(SymbolTable parent, AstNode astNode, String type){
        this.type = type;
        this.methods = new HashMap<String, MethodEntry>();
        this.variables = new HashMap<String, VarEntry>();
        this.parent = parent;
        this.astNodeInProgram = astNode;
    }

    public void addVarEntry(VariableIntroduction var, String varType){
        VarEntry entry = new VarEntry(var, varType);
        String name = var.name();
        this.variables.put(name, entry);
    }

    public void addMethodEntry(MethodDecl methodDecl){
        MethodEntry entry = new MethodEntry();
        String name = methodDecl.name();
        this.methods.put(name, entry);
    }

//    public String type(){
//        return this.type;
//    }

    public HashMap<String, MethodEntry> methods(){
        return this.methods;
    }
//
//    public HashMap<String, VarEntry> variables(){
//        return this.variables;
//    }

    public boolean hasMethodWithName(String methodName){
        return (this.methods.containsKey(methodName));
    }

    public boolean hasVariableWithName(String varName){
        return this.variables.containsKey(varName);
    }

    public AstType getVariableAstTypeOfName(String varName){
        return this.variables.get(varName).type();
    }

    public SymbolTable parent(){
        return this.parent;
    }

    public AstNode astNodeInProgram(){
        return this.astNodeInProgram;
    }

    public String GetVariableType(String varName){
        if(this.hasVariableWithName(varName)){
            AstType type = getVariableAstTypeOfName(varName);
            if (type instanceof RefType){
                return ((RefType) type).id();
            }
        }
        return null;
    }

    public boolean isMethodSymbolTable(){
        return this.type.equals("method");
    }

    public boolean isClassSymbolTable(){
        return this.type.equals("class");
    }

}
