package Ex1.RenameVisitors;

import Ex1.SearchInContext;
import Ex1.SymbolTables.VarEntry;
import ast.*;

import java.util.Set;

public class FormalAndVarDeclRenameVisitor extends RenameVisitor {



    public FormalAndVarDeclRenameVisitor(String oldName, String newName, SearchInContext searchInContext){
        super(oldName, newName, searchInContext);
    }


    @Override
    public void visit(Program program) {
        //do nothing
    }

    @Override
    public void visit(ClassDecl classDecl) {
        // do nothing
    }

    @Override
    public void visit(MainClass mainClass) {
        // do nothing
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        if (methodDecl.formals() != null){
            for (FormalArg formalArg : methodDecl.formals()){
                formalArg.accept(this);
            }
        }

        if (methodDecl.vardecls() != null){
            for (VarDecl varDecl : methodDecl.vardecls()){
                varDecl.accept(this);
            }
        }

        if (methodDecl.body() != null){
            for (Statement statement : methodDecl.body()){
                statement.accept(this); // visit(statement);
            }
        }

        methodDecl.ret().accept(this); // visit(methodDecl.ret());
    }

    @Override
    public void visit(FormalArg formalArg) {
        if (formalArg.name().equals(oldName)){
            formalArg.setName(newName);
        }
    }

    @Override
    public void visit(VarDecl varDecl) {
        if(varDecl.name().equals(oldName)){
            varDecl.setName(newName);
        }
    }

    @Override
    public void visit(BlockStatement blockStatement) {
        if (blockStatement.statements() != null){
            for (Statement statement : blockStatement.statements()){
                statement.accept(this); // visit(statement);
            }
        }
    }

    @Override
    public void visit(IfStatement ifStatement) {
        ifStatement.cond().accept(this);
        ifStatement.thencase().accept(this);
        ifStatement.elsecase().accept(this);
    }

    @Override
    public void visit(WhileStatement whileStatement) {
        whileStatement.cond().accept(this);
        whileStatement.body().accept(this);
    }

    @Override
    public void visit(SysoutStatement sysoutStatement) {
        sysoutStatement.arg().accept(this);
    }

    @Override
    public void visit(AssignStatement assignStatement) {
        if (assignStatement.lv().equals(oldName)){
            assignStatement.setLv(newName);
        }
        assignStatement.rv().accept(this);
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
        if (assignArrayStatement.lv().equals(oldName)){
            assignArrayStatement.setLv(newName);
        }
        assignArrayStatement.index().accept(this);
        assignArrayStatement.rv().accept(this);
    }

    @Override
    public void visit(AndExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(LtExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(AddExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(SubtractExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(MultExpr e) {
        e.e1().accept(this);
        e.e2().accept(this);
    }

    @Override
    public void visit(ArrayAccessExpr e) {
        e.arrayExpr().accept(this);
        e.indexExpr().accept(this);
    }

    @Override
    public void visit(ArrayLengthExpr e) {
        e.arrayExpr().accept(this);
    }

    @Override
    public void visit(MethodCallExpr e) {
        e.ownerExpr().accept(this);

        if (e.actuals() != null){
            for (Expr actual : e.actuals()){
                actual.accept(this);
            }
        }
    }

    @Override
    public void visit(IntegerLiteralExpr e) {
        // do nothing
    }

    @Override
    public void visit(TrueExpr e) {
        // do nothing
    }

    @Override
    public void visit(FalseExpr e) {
        // do nothing
    }

    @Override
    public void visit(IdentifierExpr e) {
        if (e.id().equals(oldName)){
            e.setId(newName);
        }
    }

    @Override
    public void visit(ThisExpr e) {
        // do nothing
    }

    @Override
    public void visit(NewIntArrayExpr e) {
        e.lengthExpr().accept(this);
    }

    @Override
    public void visit(NewObjectExpr e) {
        // do nothing
    }

    @Override
    public void visit(NotExpr e) {
        e.e().accept(this);
    }

    @Override
    public void visit(IntAstType t) {
        // do nothing
    }

    @Override
    public void visit(BoolAstType t) {
        // do nothing
    }

    @Override
    public void visit(IntArrayAstType t) {
        // do nothing
    }

    @Override
    public void visit(RefType t) {
        // do nothing
    }
}
