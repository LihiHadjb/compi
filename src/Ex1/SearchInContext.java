package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.SymbolTables.SymbolTable;
import ast.AstNode;
import ast.ClassDecl;

public class SearchInContext {
    public InheritanceTrees inheritanceTrees;

    public SearchInContext(InheritanceTrees inheritanceTrees){
        this.inheritanceTrees = inheritanceTrees;
    }

    public String getVariableType(SymbolTable symbolTable, String varName){
        SymbolTable currTable = symbolTable;
        String type = currTable.GetVariableTypeFromSymbolTable(varName);

        while (type == null){
            currTable = GetParentSymbolTable(currTable);
            type = currTable.GetVariableTypeFromSymbolTable(varName);
        }

        return type;
    }
    public SymbolTable GetParentSymbolTable(SymbolTable currSymbolTable) {
        // Method case
        if (currSymbolTable.type().equals("method")) {
            return currSymbolTable.parent();
        }

        // Class case
        else {
            String classId = ((ClassDecl) currSymbolTable.astNodeInProgram()).name();
            InheritanceNode currInheritanceNode = inheritanceTrees.flatClasses().get(classId);
            InheritanceNode parentInheritanceNode = currInheritanceNode.parent();
            AstNode parentAstNode = parentInheritanceNode.astNode();
            SymbolTable parentSymbolTable = parentAstNode.symbolTable();

            return parentSymbolTable;
        }
    }

}
