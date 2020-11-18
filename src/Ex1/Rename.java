package Ex1;

import Ex1.RenameVisitors.FormalAndVarDeclRenameVisitor;
import Ex1.RenameVisitors.MethodRenameVisitor;
import Ex1.RenameVisitors.FieldRenameVisitor;
import ast.*;


import java.util.HashSet;
import java.util.Set;

public class Rename {
    //public InheritanceTrees inheritanceTrees;
    private Program prog;
    private String oldName;
    private String lineNumber;
    private String newName;
    private boolean isMethod;
    private SearchInContext searchInContext;

    public Rename(Program prog, Boolean isMethod, String oldName, String lineNumber, String newName) {
        this.prog = prog;
        this.oldName = oldName;
        this.lineNumber = lineNumber;
        this.newName = newName;
        this.isMethod = isMethod;
        this.searchInContext = new SearchInContext(prog, isMethod, oldName, lineNumber);

        doRename();
    }

    private void doRename(){
        if (isMethod) {
            RenameMethod();
        }

        else {
            VariableIntroduction targetVarIntro = (VariableIntroduction) searchInContext.targetAstNode();
            String varIntroductionType = GetVarIntroductionType(targetVarIntro, searchInContext);

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
        MethodDecl targetAstNodeMethod = searchInContext.targetAstNodeMethod();
        targetAstNodeMethod.accept(formalAndVarDeclRenameVisitor);
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
