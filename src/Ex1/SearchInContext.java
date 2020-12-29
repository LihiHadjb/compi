package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.SymbolTables.MethodEntry;
import Ex1.SymbolTables.SymbolTable;
import Ex1.SymbolTables.SymbolTableBuilder;
import Ex2.Vtable;
import ast.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SearchInContext {
    private InheritanceTrees inheritanceTrees;
    private AstNode targetAstNode;
    private MethodDecl targetAstNodeMethod;
    private ClassDecl targetAstNodeClass;
    private HashMap<AstNode, SymbolTable> astNodeToSymbolTable;
    private boolean isTargetField;

    //____________________COMMON_______________________________________
    public SearchInContext(Program prog){
        this.astNodeToSymbolTable = new HashMap<>();
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder(astNodeToSymbolTable);
        symbolTableBuilder.build(prog);
        this.inheritanceTrees = new InheritanceTrees(prog);

    }

    public SearchInContext(Program prog, boolean isMethod, String oldName, String lineNumber){
        this(prog);
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber, isMethod);
        prog.accept(initTargetsVisitor);
        initTargetAstNodes(prog, oldName, lineNumber, isMethod);

    }

    private void initTargetAstNodes(Program prog, String oldName, String lineNumber, boolean isMethod){
        InitTargetsVisitor initTargetsVisitor = new InitTargetsVisitor(oldName, lineNumber, isMethod);
        prog.accept(initTargetsVisitor);
        this.targetAstNode = initTargetsVisitor.targetAstNode();
        this.targetAstNodeMethod = initTargetsVisitor.lastMethodSeen();
        this.targetAstNodeClass = initTargetsVisitor.lastClassSeen();
        this.isTargetField = initTargetsVisitor.isTargetField();
    }

    //Assumes symbolTable is of type "class"!!!
    public SymbolTable lookupSuperSymbolTable(SymbolTable symbolTable){
        InheritanceNode currInheritanceNode = classSymbolTable2InheritanceNode(symbolTable);
        InheritanceNode parentInheritanceNode = currInheritanceNode.parent();
        SymbolTable parentSymbolTable = null;
        if (parentInheritanceNode != null){
            parentSymbolTable = inheritanceNode2ClassSymbolTable(parentInheritanceNode);
        }
        return parentSymbolTable;
    }

    //find the parent of a **general** symbolTable
    public SymbolTable lookupParentSymbolTable(SymbolTable currSymbolTable) {
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
    public InheritanceNode classSymbolTable2InheritanceNode(SymbolTable symbolTable){
        ClassDecl correspondingClassAstNode = (ClassDecl)symbolTable.astNodeInProgram();
        return inheritanceTrees.classAstNode2InheritanceNode(correspondingClassAstNode);
    }

    public SymbolTable inheritanceNode2ClassSymbolTable(InheritanceNode inheritanceNode){
        AstNode classAstNode = inheritanceNode.astNode();
        SymbolTable classSymbolTable =  this.astNodeToSymbolTable.get(classAstNode);
        return classSymbolTable;

    }

    public ClassDecl classSymbolTable2ClassDecl(SymbolTable symbolTable){
        return (ClassDecl)symbolTable.astNodeInProgram();
    }

    public SymbolTable className2ClassSymbolTable(String className){
        InheritanceNode classInheritanceNode = inheritanceTrees.className2InheritanceNode(className);
        return inheritanceNode2ClassSymbolTable(classInheritanceNode);
    }




    //____________________Stuff for VARIABLES renaming_______________________________________

    public Set<String> getClassesToCheckForField(VarDecl varDecl){
        InheritanceNode targetInheritanceNodeClass = inheritanceTrees.classAstNode2InheritanceNode(targetAstNodeClass);
        Set<String> classesToCheck = inheritanceTrees.GetAllClassesUnderAncestor(targetInheritanceNodeClass);
        return classesToCheck;
    }

    public String lookupVariableType(MethodDecl context, String varName){
        SymbolTable currTable = this.astNodeToSymbolTable.get(context);
        String type;

        while (currTable != null && !currTable.hasVariableWithName(varName)){
            currTable = lookupParentSymbolTable(currTable);
        }

        if(currTable == null){
            return null;
        }

        type = currTable.GetVariableType(varName);
        return type;
    }

    //TODO: run old test to see that the null didnt break anything!!
    public AstType lookupVarAstType(MethodDecl methodDecl, String varName){
        SymbolTable currTable = this.astNodeToSymbolTable.get(methodDecl);
        AstType type;
        while (currTable != null && !currTable.hasVariableWithName(varName)){
            currTable = lookupParentSymbolTable(currTable);
        }

        if(currTable == null){
            return null;
        }

        type = currTable.getVariableAstTypeOfName(varName);
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
        SymbolTable methodSymbolTable = this.astNodeToSymbolTable.get(methodDecl);
        SymbolTable classSymbolTable = lookupParentSymbolTable(methodSymbolTable);
        ClassDecl classDecl = classSymbolTable2ClassDecl(classSymbolTable);
        return classDecl.name();
    }

    //alternative for FindAncestorClass(...)
    public InheritanceNode lookupHighestAncestorClassThatHasMethod(MethodDecl methodDecl){
        String methodName = methodDecl.name();
        SymbolTable methodSymbolTable = this.astNodeToSymbolTable.get(methodDecl);
        SymbolTable initialClassSymbolTable = lookupParentSymbolTable(methodSymbolTable);

        SymbolTable curr = initialClassSymbolTable;
        SymbolTable parent = lookupParentSymbolTable(initialClassSymbolTable);

        while(parent != null && hasMethodOrInheritedWithName(methodName, parent)){
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

    public HashMap<AstNode, SymbolTable> astNodeToSymbolTable(){ return this.astNodeToSymbolTable; }

    public boolean isTargetField(){ return this.isTargetField; }

    public InheritanceTrees inheritanceTrees(){
        return this.inheritanceTrees;
    }

    public boolean hasMethodOrInheritedWithName(String methodName, SymbolTable classSymbolTable){
        if (classSymbolTable.hasMethodWithName(methodName)){
            return true;
        }

        SymbolTable parentClassSymbolTable = lookupParentSymbolTable(classSymbolTable);
        if (parentClassSymbolTable != null){
            return hasMethodOrInheritedWithName(methodName, parentClassSymbolTable);
        }

        return false;
    }

    public MethodEntry getMethodEntryOfClosestAncestorThatHasMethod(String methodName, SymbolTable classSymbolTable){
        if (classSymbolTable.hasMethodWithName(methodName)){
            return classSymbolTable.methods().get(methodName);
        }

        SymbolTable parentClassSymbolTable = lookupParentSymbolTable(classSymbolTable);
        if (parentClassSymbolTable != null){
            return getMethodEntryOfClosestAncestorThatHasMethod(methodName, parentClassSymbolTable);
        }

        return null;
    }

    public boolean verifyOverridingMethod(MethodDecl methodDecl, ClassDecl currClass){
        SymbolTable currClassSymbolTable = astNodeToSymbolTable.get(currClass);
        SymbolTable parentClassSymbolTable = lookupParentSymbolTable(currClassSymbolTable);
        MethodEntry overridenMethodEntry = getMethodEntryOfClosestAncestorThatHasMethod(methodDecl.name(), parentClassSymbolTable);

        if(overridenMethodEntry == null){//first time this method is declared in hierarchy
            return true;
        }

        MethodEntry overridingMethodEntry = currClassSymbolTable.methods().get(methodDecl.name());
        MethodDecl overridingDecl = overridingMethodEntry.getMethodDecl();
        MethodDecl overridenDecl = overridenMethodEntry.getMethodDecl();

        if (checkMethodsArgumentsSize(overridingDecl, overridenDecl) &&
                checkMethodsArguments(overridingDecl, overridenDecl) &&
                checkMethodsReturnValues(overridingDecl, overridenDecl)){
            return true;
        }

        return false;
    }

    public boolean checkMethodsArgumentsSize(MethodDecl overridingDecl, MethodDecl overridenDecl){
        //both not null
        if(overridenDecl.formals() != null && overridingDecl.formals() != null && overridenDecl.formals().size() != overridingDecl.formals().size()){
            return false;
        }

        //only one of them is null
        else if(overridingDecl.formals() == null || overridenDecl.formals() != null){
            if (overridenDecl.formals().size() > 0){
                return false;
            }
        }
        else if(overridingDecl.formals() != null || overridenDecl.formals() == null){
            if (overridingDecl.formals().size() > 0){
                return false;
            }
        }

        return true;
    }

    public boolean checkMethodsArguments(MethodDecl overridingDecl, MethodDecl overridenDecl){
        if (overridenDecl.formals() != null){
            for (int i=0 ; i < overridenDecl.formals().size() ; i++){
                if (overridenDecl.formals().get(i).type().equals(overridingDecl.formals().get(i).type())){ //TODO: make sure equals works as expected
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSubType(AstType actual, AstType expected){
        if((expected == null && actual != null) ||
                (expected != null && actual == null)){
            return false;
        }

        if(actual instanceof IntAstType){
            if(!(expected instanceof IntAstType)){
                return false;
            }
        }

        if(actual instanceof BoolAstType){
            if(!(expected instanceof BoolAstType)){
                return false;
            }
        }

        if(actual instanceof IntArrayAstType){
            if(!(expected instanceof IntArrayAstType)){
                return false;
            }
        }

        if(actual instanceof RefType){
            if(!(expected instanceof RefType)){
                return false;
            }
        }

        //TODO: how do we know the type is null??

        RefType expectedRefType = (RefType)expected;
        RefType actualRefType = (RefType)actual;
        if(!isSubClass(actualRefType, expectedRefType)){
            return false;
        }
        return true;


    }

    public boolean isSubClass(RefType child, RefType parent){
        InheritanceNode parentInheritanceNode = inheritanceTrees.className2InheritanceNode(parent.id());
        InheritanceNode childInheritanceNode = inheritanceTrees.className2InheritanceNode(child.id());

        Set<String> allPossibleSubTypes = this.inheritanceTrees.GetAllClassesUnderAncestor(parentInheritanceNode);
        return allPossibleSubTypes.contains(childInheritanceNode.name());


    }

    public boolean checkMethodsReturnValues(MethodDecl overridingDecl, MethodDecl overridenDecl){
        return isSubType(overridingDecl.returnType(), overridenDecl.returnType());
//        if((overridenDecl.returnType() == null && overridingDecl.returnType() != null) ||
//                (overridenDecl.returnType() != null && overridingDecl.returnType() == null)){
//            return false;
//        }
//
//        if (overridenDecl.ret() != null){
//            if (!(overridenDecl.returnType() instanceof RefType)){
//                if (overridenDecl.returnType().equals(overridingDecl.returnType())){ //TODO: make sure equals works as expected
//                    return false;
//                }
////            }
////            else{
//                SymbolTable overridenMethodSymbolTable = astNodeToSymbolTable.get(overridenDecl);
//                SymbolTable overridenClassSymbolTable = lookupParentSymbolTable(overridenMethodSymbolTable);
//                InheritanceNode overriddenClassInheritanceNode = classSymbolTable2InheritanceNode(overridenClassSymbolTable);
//                RefType overridingRetType = (RefType)overridingDecl.returnType();
//                RefType overriddenRetType =
//                Set<String> allPossibleSubTypes = this.inheritanceTrees.GetAllClassesUnderAncestor(overriddenClassInheritanceNode);
//                if (!allPossibleSubTypes.contains(overridingRetType.id())){
//                    return false;
//                }
//            }
//        }

    }
}



