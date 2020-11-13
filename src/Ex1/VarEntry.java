package Ex1;

import ast.AstType;
import ast.IntAstType;
import ast.VarDecl;
import ast.VariableIntroduction;

public class VarEntry {
    AstType type;

    public VarEntry(VariableIntroduction var){
        this.type = var.type();
    }
}
