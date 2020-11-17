package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.RenameVisitors.FormalAndVarDeclRenameVisitor;
import Ex1.RenameVisitors.MethodRenameVisitor;
import Ex1.RenameVisitors.FieldRenameVisitor;
import Ex1.SymbolTables.SymbolTableBuilder;
import Ex1.SymbolTables.VarEntry;
import ast.*;


import java.util.HashSet;
import java.util.Set;

public class Rename {
    public InheritanceTrees inheritanceTrees;
    public Program prog;
    public String oldName;
    public String lineNumber;
    public String newName;
    public String newFile;
    AstNode targetAstNode;
    MethodDecl targetAstNodeMethod;
    ClassDecl targetAstNodeClass;
    SearchInContext searchInContext;

    public Rename(Program prog, Boolean isMethod, String oldName, String lineNumber, String newName) {
        this.inheritanceTrees = new InheritanceTrees(prog);
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        symbolTableBuilder.build(prog);
        this.prog = prog;
        this.oldName = oldName;
        this.lineNumber = lineNumber;
        this.newName = newName;
        this.searchInContext = new SearchInContext(inheritanceTrees);
        this.targetAstNode = searchInContext.SearchTargetAstNode(); // search for astNode using lineNumber:
        // visit prog until you get to the right node. While searching, save last methos & last class
        this.targetAstNodeMethod = searchInContext.getTargetAstNodeMethod();
        this.targetAstNodeClass = searchInContext.getTargetAstNodeClass();

        if (isMethod) {
            RenameMethod();
        } else {
            String varIntroductionType = searchInContext.GetVarIntroductionType((VariableIntroduction)targetAstNode, targetAstNodeMethod, targetAstNodeClass);

            if (varIntroductionType.equals("field")){
                RenameField();
            }
            else{ // in case it's formalParameter OR varDecl
                RenameFormalOrVarDecl();
            }
        }
    }

    public void RenameMethod() {
        InheritanceNode highestAncestor = searchInContext.FindAncestorClass(targetAstNodeClass); //TODO: verify Tslil
        Set<String> classesToCheck = GetAllClassesUnderAncestor(highestAncestor);
        MethodRenameVisitor methodRenameVisitor = new MethodRenameVisitor(prog, classesToCheck, oldName, newName, searchInContext);
        methodRenameVisitor.visit(prog);
    }

    public void RenameField() {
        InheritanceNode targetInheritanceNodeClass = searchInContext.GetInheritanceNodeOfAstNode((ClassDecl)targetAstNodeClass);
        Set<String> classesToCheck = GetAllClassesUnderAncestor(targetInheritanceNodeClass);
        FieldRenameVisitor fieldRenameVisitor = new FieldRenameVisitor(prog, classesToCheck, oldName, newName, searchInContext);
        fieldRenameVisitor.visit(prog);
    }

    public void RenameFormalOrVarDecl(){
        FormalAndVarDeclRenameVisitor formalAndVarDeclRenameVisitor = new FormalAndVarDeclRenameVisitor(oldName, newName, searchInContext);
        formalAndVarDeclRenameVisitor.visit(targetAstNodeMethod);
    }

    public Set<String> GetAllClassesUnderAncestor(InheritanceNode highestAncestor) {
        Set<String> classesToCheck = new HashSet<>();
        classesToCheck.add(highestAncestor.name());

        if (highestAncestor.children().isEmpty()) {
            return classesToCheck;
        }

        for (InheritanceNode child : highestAncestor.children()) {
            classesToCheck.addAll(GetAllClassesUnderAncestor(child));
        }

        return classesToCheck;
    }
}
