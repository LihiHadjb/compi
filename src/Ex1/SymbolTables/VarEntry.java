package Ex1.SymbolTables;

import ast.AstType;
import ast.VariableIntroduction;

public class VarEntry {
    AstType type;

    public VarEntry(VariableIntroduction var){
        this.type = var.type();
    }
}
