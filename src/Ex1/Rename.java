package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.RenameVisitors.FormalAndVarDeclRenameVisitor;
import Ex1.RenameVisitors.MethodRenameVisitor;
import Ex1.RenameVisitors.FieldRenameVisitor;
//import Ex1.SymbolTables.SymbolTableBuilder;
import ast.*;


import java.util.HashSet;
import java.util.Set;

public class Rename {
    //public InheritanceTrees inheritanceTrees;
    private Program prog;
    private String oldName;
    private String lineNumber;
    private String newName;
    SearchInContext searchInContext;

    public Rename(Program prog, Boolean isMethod, String oldName, String lineNumber, String newName) {
        //this.inheritanceTrees = new InheritanceTrees(prog);
        //SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        //symbolTableBuilder.build(prog);
        this.prog = prog;
        this.oldName = oldName;
        this.lineNumber = lineNumber;
        this.newName = newName;
        this.searchInContext = new SearchInContext(prog, isMethod, oldName, lineNumber);

        doRename(prog, isMethod, oldName, lineNumber, newName);
    }

    private void doRename(Program prog, Boolean isMethod, String oldName, String lineNumber, String newName){
        if (isMethod) {
            RenameMethod();
        }

        else {
            VariableIntroduction targetVarIntroduction = (VariableIntroduction) searchInContext.targetAstNode();
            String varIntroductionType = GetVarIntroductionType(targetVarIntroduction, searchInContext);

            if (varIntroductionType.equals("field")){
                RenameField();
            }
            else{ // in case it's formalParameter OR varDecl
                RenameFormalOrVarDecl();
            }
        }
    }


    private void RenameMethod() {
        MethodRenameVisitor methodRenameVisitor = new MethodRenameVisitor(oldName, newName, searchInContext);
        prog.accept(methodRenameVisitor);
    }

    private void RenameField() {
        FieldRenameVisitor fieldRenameVisitor = new FieldRenameVisitor(oldName, newName, searchInContext);
        prog.accept(fieldRenameVisitor);
    }

    private void RenameFormalOrVarDecl(){
        FormalAndVarDeclRenameVisitor formalAndVarDeclRenameVisitor = new FormalAndVarDeclRenameVisitor(oldName, newName, searchInContext);
        prog.accept(formalAndVarDeclRenameVisitor);
    }

    private String GetVarIntroductionType(VariableIntroduction targetAstNode, SearchInContext searchInContext){
        MethodDecl targetAstNodeMethod = searchInContext.targetAstNodeMethod();
        if (targetAstNodeMethod.vardecls().contains(targetAstNode)){
            return "varDecl";
        }
        if (targetAstNodeMethod.formals().contains(targetAstNode)){
            return "formal";
        }
        else{
            return "field";
        }
    }


}
