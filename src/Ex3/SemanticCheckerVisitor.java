package Ex3;

import Ex1.SearchInContext;
import Ex1.SymbolTables.MethodEntry;
import Ex1.SymbolTables.SymbolTable;
import ast.*;
import org.w3c.dom.css.ViewCSS;

import java.util.HashSet;
import java.util.Set;

public class SemanticCheckerVisitor implements Visitor {
    SearchInContext searchInContext;
    boolean isErrorFound;
    Set<String> classesSeen;
    ClassDecl lastClassSeen;
    MethodDecl lastMethodSeen;
    String mainClassName;

    public boolean isErrorFound() {
        return isErrorFound;
    }

    public SemanticCheckerVisitor(SearchInContext searchInContext){
        this.searchInContext = searchInContext;
        this.isErrorFound = false;
        this.classesSeen = new HashSet<>();
    }


    @Override
    public void visit(Program program) {
        program.mainClass().accept(this);
        if(program.classDecls() != null){
            for(ClassDecl classDecl : program.classDecls()){
                classDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(ClassDecl classDecl) {
        this.lastClassSeen = classDecl;
        //check duplicates
        if(this.classesSeen.contains(classDecl.name())){
            this.isErrorFound = true;
            return;
        }

        //check that super class was defined before the current class
        if (classDecl.superName() != null &&
                !this.classesSeen.contains(classDecl.superName()) &&
                !classDecl.superName().equals(this.mainClassName)) {
            this.isErrorFound = true;
            return;
        }

        classesSeen.add(classDecl.name());

        if(classDecl.fields() != null){
            Set<String> currClassFields = new HashSet<>();
            for(VarDecl field : classDecl.fields()){
                if(currClassFields.contains(field.name())){
                    this.isErrorFound = true;
                    return;
                }
                currClassFields.add(field.name());
                field.accept(this);
            }
        }

        if(classDecl.methoddecls() != null){
            Set<String> currClassMethods = new HashSet<>();
            for(MethodDecl methodDecl : classDecl.methoddecls()){
                if(currClassMethods.contains(methodDecl.name())){
                    this.isErrorFound = true;
                    return;
                }
                currClassMethods.add(methodDecl.name());
                methodDecl.accept(this);
            }
        }

    }

    @Override
    public void visit(MainClass mainClass) {
        this.mainClassName = mainClass.name();
        this.classesSeen.add(mainClass.name());
        mainClass.mainStatement().accept(this);

    }

    @Override
    public void visit(MethodDecl methodDecl) {
        //check that overriding is legal
        if (!(searchInContext.verifyOverridingMethod(methodDecl, this.lastClassSeen))){
            this.isErrorFound = true;
        }

        //TODO: STOPPED HERE!!

    }

    @Override
    public void visit(FormalArg formalArg) {

    }

    @Override
    public void visit(VarDecl varDecl) {

    }

    @Override
    public void visit(BlockStatement blockStatement) {

    }

    @Override
    public void visit(IfStatement ifStatement) {

    }

    @Override
    public void visit(WhileStatement whileStatement) {

    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {

    }

    @Override
    public void visit(AssignStatement assignStatement) {

    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {

    }

    @Override
    public void visit(AndExpr e) {

    }

    @Override
    public void visit(LtExpr e) {

    }

    @Override
    public void visit(AddExpr e) {

    }

    @Override
    public void visit(SubtractExpr e) {

    }

    @Override
    public void visit(MultExpr e) {

    }

    @Override
    public void visit(ArrayAccessExpr e) {

    }

    @Override
    public void visit(ArrayLengthExpr e) {

    }

    @Override
    public void visit(MethodCallExpr e) {

    }

    @Override
    public void visit(IntegerLiteralExpr e) {

    }

    @Override
    public void visit(TrueExpr e) {

    }

    @Override
    public void visit(FalseExpr e) {

    }

    @Override
    public void visit(IdentifierExpr e) {

    }

    @Override
    public void visit(ThisExpr e) {

    }

    @Override
    public void visit(NewIntArrayExpr e) {

    }

    @Override
    public void visit(NewObjectExpr e) {

    }

    @Override
    public void visit(NotExpr e) {

    }

    @Override
    public void visit(IntAstType t) {

    }

    @Override
    public void visit(BoolAstType t) {

    }

    @Override
    public void visit(IntArrayAstType t) {

    }

    @Override
    public void visit(RefType t) {

    }
}
