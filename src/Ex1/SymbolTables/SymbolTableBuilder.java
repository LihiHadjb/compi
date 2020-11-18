package Ex1.SymbolTables;

import ast.*;

public class SymbolTableBuilder{

    public void build(Program program) {
        if (program.classDecls() != null){
            for (ClassDecl classdecl : program.classDecls()) {
                buildClassSymbolTable(classdecl);
            }
        }
    }

    private void buildClassSymbolTable(ClassDecl classDecl) {
            SymbolTable curr = new SymbolTable(null, classDecl, "class");
            if (classDecl.fields() != null){
                for (VarDecl field : classDecl.fields()) {
                    curr.addVarEntry(field, "field");
                }
            }

            if (classDecl.methoddecls() != null){
                for (MethodDecl methodDecl : classDecl.methoddecls()){
                    curr.addMethodEntry(methodDecl);
                    buildMethodSymbolTable(curr, methodDecl);
                }
            }

            classDecl.setSymbolTable(curr);

    }

    private void buildMethodSymbolTable(SymbolTable parent, MethodDecl methodDecl) {
        SymbolTable curr = new SymbolTable(parent, methodDecl, "method");

        if (methodDecl.formals() != null){
            for (FormalArg formalArg : methodDecl.formals()){
                curr.addVarEntry(formalArg, "formal");
            }
        }

        if (methodDecl.vardecls() != null){
            for (VarDecl varDecl : methodDecl.vardecls()) {
                curr.addVarEntry(varDecl, "varDecl");
            }
        }

        methodDecl.setSymbolTable(curr);
    }

}
