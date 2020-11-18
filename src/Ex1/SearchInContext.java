package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.SymbolTables.SymbolTable;
import Ex1.SymbolTables.SymbolTableBuilder;
import ast.*;

import java.util.Set;

public class SearchInContext {
    private InheritanceTrees inheritanceTrees;
    private AstNode targetAstNode;
    private MethodDecl targetAstNodeMethod;
    private ClassDecl targetAstNodeClass;

    //____________________COMMON_______________________________________

    public SearchInContext(Program prog, boolean isMethod, String oldName, String lineNumber){
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        symbolTableBuilder.build(prog);
        this.inheritanceTrees = new InheritanceTrees(prog);
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);
        initTargetAstNodes(prog, oldName, lineNumber);

    }

    private void initTargetAstNodes(Program prog, String oldName, String lineNumber){
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber);
        prog.accept(initTargetsVisitor);
        this.targetAstNode = initTargetsVisitor.targetAstNode();
        this.targetAstNodeMethod = initTargetsVisitor.lastMethodSeen();
        this.targetAstNodeClass = initTargetsVisitor.lastClassSeen();
    }

    //Assumes symbolTable is of type "class"!!!
    private SymbolTable lookupSuperSymbolTable(SymbolTable symbolTable){
        InheritanceNode currInheritanceNode = classSymbolTable2InheritanceNode(symbolTable);
        InheritanceNode parentInheritanceNode = currInheritanceNode.parent();
        SymbolTable parentSymbolTable = inheritanceNode2ClassSymbolTable(parentInheritanceNode);
        return parentSymbolTable;
    }

    //find the parent of a **general** symbolTable
    private SymbolTable lookupParentSymbolTable(SymbolTable currSymbolTable) {
        // Method case
        if(currSymbolTable.isMethodSymbolTable()){
            return currSymbolTable.parent();
        }

        // Class case
        else {
            return this.lookupSuperSymbolTable(currSymbolTable);
        }
    }

    //Assumes symbolTable is of type "class"!!!
    private InheritanceNode classSymbolTable2InheritanceNode(SymbolTable symbolTable){
        ClassDecl correspondingClassAstNode = (ClassDecl)symbolTable.astNodeInProgram();
        return inheritanceTrees.classAstNode2InheritanceNode(correspondingClassAstNode);
    }

    private SymbolTable inheritanceNode2ClassSymbolTable(InheritanceNode inheritanceNode){
        AstNode classAstNode = inheritanceNode.astNode();
        return classAstNode.symbolTable();

    }

    private ClassDecl classSymbolTable2ClassDecl(SymbolTable symbolTable){
        return (ClassDecl)symbolTable.astNodeInProgram();
    }


    //____________________Stuff for VARIABLES renaming_______________________________________

    public Set<String> getClassesToCheckForField(VarDecl varDecl){
        InheritanceNode targetInheritanceNodeClass = inheritanceTrees.classAstNode2InheritanceNode(targetAstNodeClass);
        Set<String> classesToCheck = inheritanceTrees.GetAllClassesUnderAncestor(targetInheritanceNodeClass);
        return classesToCheck;
    }

    public String lookupVariableType(MethodDecl context, String varName){
        SymbolTable currTable = context.symbolTable();
        String type;

        while (!currTable.hasVariableWithName(varName)){
            currTable = lookupParentSymbolTable(currTable);
        }

        type = currTable.GetVariableType(varName);
        return type;
    }


    //____________________Stuff for METHOD renaming_______________________________________

    public Set<String> getClassesToCheckForMethod(MethodDecl methodDecl){
        InheritanceNode highestAncestor = lookupHighestAncestorClassThatHasMethod(methodDecl);
        Set<String> classesToCheck = inheritanceTrees.GetAllClassesUnderAncestor(highestAncestor);
        return classesToCheck;
    }

    public String lookupClassNameOfMethod(MethodDecl methodDecl){
        // In order to find the class that associated with "this" expression we need to go the
        // class in which the method was declared and find out what is the name/id of the class.
        // Meaning, only need to go up once to the symbol table of the parent class --> go to the AstNode of the class
        // --> get it's name (which is basically the type of "this" that we need to find)
        SymbolTable methodSymbolTable = methodDecl.symbolTable();
        SymbolTable classSymbolTable = lookupParentSymbolTable(methodSymbolTable);
        ClassDecl classDecl = classSymbolTable2ClassDecl(classSymbolTable);
        return classDecl.name();
    }

    //alternative for FindAncestorClass(...)
    public InheritanceNode lookupHighestAncestorClassThatHasMethod(MethodDecl methodDecl){
        String methodName = methodDecl.name();
        SymbolTable methodSymbolTable = methodDecl.symbolTable();
        SymbolTable initialClassSymbolTable = lookupParentSymbolTable(methodSymbolTable);

        SymbolTable curr = initialClassSymbolTable;
        SymbolTable parent = lookupParentSymbolTable(initialClassSymbolTable);

        while(parent != null && curr.hasMethodWithName(methodName)){
            curr = parent;
            parent = lookupParentSymbolTable(curr);
        }

        return classSymbolTable2InheritanceNode(curr);
    }




//    //Assumes targetAstNode is a MethodDecl!!!
//    public InheritanceNode FindAncestorClass(AstNode targetAstNodeClass){
//        //In this case targetAstNode is a MethodDecl and we need to find it's highest ancestor class. So we should go
//        // to targetAstNode's parent (which is a ClassDecl node) --> find it's name --> get relevant InheritanceNode
//        // of this class from flatClasses --> go up from parent to parent until we get to null (which means we got to
//        // the highest ancestor) --> return the highestAncestor
//        InheritanceNode currClassInheritanceNode = GetInheritanceNodeOfAstNode((ClassDecl)targetAstNodeClass);
//        InheritanceNode parentClassInheritanceNode = currClassInheritanceNode.parent();
//
//        while (parentClassInheritanceNode != null){
//            currClassInheritanceNode = parentClassInheritanceNode;
//            parentClassInheritanceNode = currClassInheritanceNode.parent();
//        }
//
//        return currClassInheritanceNode;
//    }


    public AstNode targetAstNode(){
        return this.targetAstNode;
    }

    public MethodDecl targetAstNodeMethod(){
        return this.targetAstNodeMethod;
    }

    public ClassDecl targetAstNodeClass(){
        return this.targetAstNodeClass;
    }


}
