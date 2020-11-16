package Ex1.RenameVisitors;

import Ex1.SearchInContext;
import Ex1.SymbolTables.VarEntry;
import ast.*;

import java.util.Set;

//TODO: currently only for field case
public class VariableRenameVisitor implements Visitor {
    private Program prog;
    private Set<String> classesToCheck;
    private String oldName;
    private String newName;
    private MethodDecl lastMethodSeen;
    private boolean isLastOwnerInClassesToCheck;
    SearchInContext searchInContext;


    public VariableRenameVisitor(Program prog, Set<String> classesToCheck, String oldName, String newName, SearchInContext searchInContext){
        this.prog = prog;
        this.classesToCheck = classesToCheck;
        this.oldName = oldName;
        this.newName = newName;
        this.searchInContext = searchInContext;
    }

    @Override
    public void visit(Program prog) {
        prog.mainClass().accept(this); // visit(prog.mainClass());
        if (prog.classDecls() != null){
            for (ClassDecl classDecl : prog.classDecls()){
                classDecl.accept(this); // visit(classDecl);
            }
        }
    }

    @Override
    public void visit(ClassDecl classDecl) {
        if (classesToCheck.contains(classDecl.name())){
            if (classDecl.fields() != null){
                for (VarDecl field : classDecl.fields()){
                    if (field.name().equals(oldName)){
                        field.setName(newName);
                    }
                }
            }

            if (classDecl.methoddecls() != null){
                for (MethodDecl methodDecl : classDecl.methoddecls()){
                    methodDecl.accept(this); // visit(methodDecl);
                }
            }
        }
    }

    @Override
    public void visit(MainClass mainClass) {
        // do nothing
    }

    @Override
    public void visit(MethodDecl methodDecl) {
        this.lastMethodSeen = methodDecl;
        boolean isHidingVarExists = false;

        isHidingVarExists = !methodDecl.symbolTable().variables().containsKey(oldName);

        if (!isHidingVarExists){
            if (methodDecl.body() != null){
                for (Statement statement : methodDecl.body()){
                    statement.accept(this); // visit(statement);
                }
            }

            methodDecl.ret().accept(this); // visit(methodDecl.ret());
        }
    }

    @Override
    public void visit(FormalArg formalArg) {
        // do nothing
    }

    @Override
    public void visit(VarDecl varDecl) {
        //do nothing
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
        assignStatement.rv().accept(this);
    }

    @Override
    public void visit(AssignArrayStatement assignArrayStatement) {
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
    public void visit(IdentifierExpr e) { //TODO: VERIFY!!!
        if (e.id().equals(oldName)){
           e.setId(newName);
        }
    }

    @Override
    public void visit(ThisExpr e) {
        //do nothing
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
