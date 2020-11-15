package Ex1;

import Ex1.Inheritance.InheritanceNode;
import Ex1.Inheritance.InheritanceTrees;
import Ex1.RenameVisitors.MethodRenameVisitor;
import Ex1.SymbolTables.SymbolTable;
import Ex1.SymbolTables.SymbolTableBuilder;
import ast.AstNode;
import ast.AstXMLSerializer;
import ast.ClassDecl;
import ast.Program;
import com.sun.jdi.request.MethodEntryRequest;


import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rename {
    public InheritanceTrees inheritanceTrees;
    public Program prog;
    public String oldName;
    public String lineNumber;
    public String newName;
    public String newFile;
    AstNode targetAstNode;
    SearchInContext searchInContext;

    public Rename(Program prog, Boolean isMethod, String oldName, String lineNumber, String newName, String newFile) {
        this.inheritanceTrees = new InheritanceTrees(prog);
        SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
        symbolTableBuilder.build(prog);
        this.prog = prog;
        this.oldName = oldName;
        this.lineNumber = lineNumber;
        this.newName = newName;
        this.newFile = newFile;
        this.targetAstNode = SearchTargetAstNode();
        this.searchInContext = new SearchInContext(inheritanceTrees);

        if (isMethod) {
            RenameMethod();
        } else {
            RenameVariable(); //inside split to 3 cases/methods: formalParameter, varDecl, Field
        }
        //think how to return/update new xml
    }

    public void RenameMethod() {
        InheritanceNode highestAncestor = FindAncestorClassForMethod();
        Set<String> classesToCheck = GetAllClassesUnderAncestor(highestAncestor);
        MethodRenameVisitor methodRenameVisitor = new MethodRenameVisitor(prog, classesToCheck, oldName, newName, searchInContext);
        methodRenameVisitor.visit(prog);


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
