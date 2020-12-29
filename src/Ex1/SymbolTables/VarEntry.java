package Ex1.SymbolTables;

import ast.AstType;
import ast.VariableIntroduction;

public class VarEntry {
    AstType type;
    boolean isFormal;

    public boolean isFormal() {
        return isFormal;
    }

    public boolean isVarDecl() {
        return isVarDecl;
    }

    public boolean isField() {
        return isField;
    }

    boolean isVarDecl;
    boolean isField;

    public VarEntry(VariableIntroduction var, String varType){
        this.type = var.type();
        this.isFormal = varType.equals("formal");
        this.isVarDecl = varType.equals("varDecl");
        this.isField = varType.equals("field");
    }

    public AstType type(){
        return this.type;
    }
}
