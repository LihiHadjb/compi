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

    // TODO: verify Tslil
    public String getContextClass(SymbolTable methodSymbolTable){
        // In order to find the class that associated with "this" expression we need to go the
        // class in which the method was declared and find out what is the name/id of the class.
        // Meaning, only need to go up once to the symbol table of the parent class --> go to the AstNode of the class
        // --> get it's name (which is basically the type of "this" that we need to find)

        SymbolTable parentClassSymbolTable = methodSymbolTable.parent();
        AstNode parentClassAstNode = parentClassSymbolTable.astNodeInProgram();
        return ((ClassDecl)parentClassAstNode).name(); //casting from AstNode to ClassDecl so we can call name() func
    }

    //TODO: verify Tslil
    public InheritanceNode FindAncestorClass(AstNode targetAstNodeClass){
        //In this case targetAstNode is a MethodDecl and we need to find it's highest ancestor class. So we should go
        // to targetAstNode's parent (which is a ClassDecl node) --> find it's name --> get relevant InheritanceNode
        // of this class from flatClasses --> go up from parent to parent until we get to null (which means we got to
        // the highest ancestor) --> return the highestAncestor
        InheritanceNode currClassInheritanceNode = GetInheritanceNodeOfAstNode((ClassDecl)targetAstNodeClass);
        InheritanceNode parentClassInheritanceNode = currClassInheritanceNode.parent();

        while (parentClassInheritanceNode != null){
            currClassInheritanceNode = parentClassInheritanceNode;
            parentClassInheritanceNode = currClassInheritanceNode.parent();
        }

        return currClassInheritanceNode;
    }

    public InheritanceNode GetInheritanceNodeOfAstNode(ClassDecl classAstNode){
        String classId = classAstNode.name();
        InheritanceNode classInheritanceNode = inheritanceTrees.flatClasses().get(classId);

        return classInheritanceNode;
    }

}
