package Ex1.SymbolTables;

import ast.MethodDecl;

public class MethodEntry {
    private MethodDecl methodDecl;

    public MethodEntry(MethodDecl methodDecl){
        this.methodDecl = methodDecl;
    }

    public MethodDecl getMethodDecl() {
        return methodDecl;
    }

    public void setMethodDecl(MethodDecl methodDecl) {
        this.methodDecl = methodDecl;
    }

}

