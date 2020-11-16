package Ex1.SymbolTables;

import ast.AstType;
import ast.VariableIntroduction;

public class VarEntry {
    AstType type;
    String varType;

    public VarEntry(VariableIntroduction var, String varType){
        this.type = var.type();
        this.varType = varType;
    }

    public AstType type(){
        return this.type;
    }
}
