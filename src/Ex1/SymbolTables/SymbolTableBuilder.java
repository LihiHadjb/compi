package Ex1.SymbolTables;

import ast.*;

public class SymbolTableBuilder{

    public void build(Program program) {
        for (ClassDecl classdecl : program.classDecls()) {
            buildClassSymbolTable(classdecl);
        }
    }

    public void buildClassSymbolTable(ClassDecl classDecl) {
            SymbolTable curr = new SymbolTable(null, classDecl, "class");
            for (VarDecl field : classDecl.fields()) {
                curr.addVarEntry(field);
            }
            for (MethodDecl methodDecl : classDecl.methoddecls()){
                curr.addMethodEntry(methodDecl);
                buildMethodSymbolTable(curr, methodDecl);
            }
            classDecl.setSymbolTable(curr);

    }

    public void buildMethodSymbolTable(SymbolTable parent, MethodDecl methodDecl) {
        SymbolTable curr = new SymbolTable(parent, methodDecl, "method");
        for (FormalArg formalArg : methodDecl.formals()){
            curr.addVarEntry(formalArg);
        }

        for (VarDecl varDecl : methodDecl.vardecls()) {
            curr.addVarEntry(varDecl);
        }
        methodDecl.setSymbolTable(curr);
    }








}
