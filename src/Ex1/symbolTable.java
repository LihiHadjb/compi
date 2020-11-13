package Ex1;

import ast.AstNode;

import java.util.HashMap;

public class symbolTable {
    String name;
    AstNode type;
//    HashMap<String, MethodEntry> methods;
//    HashMap<String, VariableEntry> variables;
    symbolTable parent;
    AstNode astNodeInProgram;

    public symbolTable(AstNode astNode, symbolTable parent){
//        this.methods = buildMethods(astNode);
//        this.variables = buildVariables(astNode);
        this.parent = parent;
    }

}
